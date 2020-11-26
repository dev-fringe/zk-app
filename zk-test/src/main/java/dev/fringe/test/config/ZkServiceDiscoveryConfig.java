package dev.fringe.test.config;

import java.net.InetAddress;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.SneakyThrows;
import lombok.val;

@Configuration
@EnableScheduling
public class ZkServiceDiscoveryConfig {

	@Value("${server.port:8080}") int port; 
	@Value("${server.name:serviceA}") String serverName;
	@Autowired CuratorFramework curatorRetry;
	ServiceInstance<String> instance;
	
    @Bean
    public ServiceDiscovery<String> serviceDiscovery() {
        return ServiceDiscoveryBuilder.builder(String.class).client(curatorRetry).basePath("servers").build();  
    }
    
	@SneakyThrows
	@PostConstruct
	public void init() {	
		val host = InetAddress.getLocalHost().getHostAddress();
		val uriSpec = new UriSpec("http://" + host + ":" + port);
        instance = ServiceInstance.<String>builder()
	        		.name(serverName)
	                .payload("")
	                .address(host)
	                .port(port)
	                .uriSpec(uriSpec)
	                .build();
        serviceDiscovery().registerService(instance);
	}
	
	@Scheduled(initialDelay = 3000, fixedDelay = 1000)
	@SneakyThrows
	public void sched() {	
        val host = InetAddress.getLocalHost().getHostAddress();
        val uriSpec = new UriSpec("http://" + host + ":" + port);
        instance = ServiceInstance.<String>builder()
            		.name(serverName)
                    .payload("")
                    .address(host)
                    .port(port)
                    .uriSpec(uriSpec)
                    .build();
        serviceDiscovery().updateService(instance);
	}
}

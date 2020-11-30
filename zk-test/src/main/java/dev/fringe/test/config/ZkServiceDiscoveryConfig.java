package dev.fringe.test.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 
 */
@Configuration
public class ZkServiceDiscoveryConfig {

	@Autowired CuratorFramework curatorRetry;
	
    @Bean
    public ServiceDiscovery<String> serviceDiscovery() {
        return ServiceDiscoveryBuilder.builder(String.class).client(curatorRetry).basePath("servers").build();  
    }
    
}

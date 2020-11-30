package dev.fringe.test;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import dev.fringe.test.config.ZkCuratorConfig;
import dev.fringe.test.config.ZkServiceDiscoveryConfig;
import dev.fringe.test.service.ZkDiscoveryService;
import lombok.SneakyThrows;

@Configuration
@ComponentScan("dev.fringe.test.service")
@PropertySource("classpath:app.properties")
@Import({ZkCuratorConfig.class, ZkServiceDiscoveryConfig.class})
public class ClientMain implements InitializingBean{

	@Autowired ZkDiscoveryService discoveryService;
	@Value("${server.name:serviceA}") String serverName;
	
	@SneakyThrows
	public static void main(String[] args){
		new AnnotationConfigApplicationContext(ClientMain.class);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println(discoveryService.findService(serverName));
	}
}

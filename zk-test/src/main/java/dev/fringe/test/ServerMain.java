package dev.fringe.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import dev.fringe.test.config.ZkCuratorConfig;
import dev.fringe.test.config.ZkServiceDiscoveryConfig;
import dev.fringe.test.scheduled.ZkRegisterJob;

@Configuration
@ComponentScan("dev.fringe.test.service")
@PropertySource("classpath:app.properties")
@Import({ZkCuratorConfig.class, ZkServiceDiscoveryConfig.class, ZkRegisterJob.class})
public class ServerMain {

	public static void main(String[] args){
		new AnnotationConfigApplicationContext(ServerMain.class);
	}

}

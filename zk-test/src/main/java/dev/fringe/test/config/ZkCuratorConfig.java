package dev.fringe.test.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZkCuratorConfig {

	
	private static String CONNECTION_STR = "127.0.0.1:2183,127.0.0.1:2182,127.0.0.1:2181";
	private static String NAMESPACE = "curator";
	
	@Bean
	public CuratorFramework curator() {
		CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(CONNECTION_STR).retryPolicy(new ExponentialBackoffRetry(1000, 3)).namespace(NAMESPACE).build();
		curatorFramework.start();
		return curatorFramework;
	}

}

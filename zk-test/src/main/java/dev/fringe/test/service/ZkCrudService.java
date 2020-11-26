package dev.fringe.test.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;

@Service
public class ZkCrudService {

	@Autowired CuratorFramework curator;
	
	@SneakyThrows
	public String create(String path, CreateMode createMode, String data) {
		return curator.create().creatingParentsIfNeeded().withMode(createMode).forPath(path, data.getBytes());
	}
	
	@SneakyThrows
	public String create(String path, CreateMode createMode) {
		return curator.create().creatingParentsIfNeeded().withMode(createMode).forPath(path);
	}
	
	@SneakyThrows
	public String create(String path) {
		return this.create(path, CreateMode.PERSISTENT);
	}
	
	@SneakyThrows
	public String create(String path, String data) {
		return this.create(path, CreateMode.PERSISTENT, data);
	}
	
	@SneakyThrows
	public Stat update(String path, String data) {
		return curator.setData().forPath(path, data.getBytes());
	}
	
	@SneakyThrows
	public String delete(String path) {
		return new String(curator.getData().forPath(path));
	}
	
	@SneakyThrows
	private String select(String path) {
		return new String(curator.getData().forPath(path));
	}
}

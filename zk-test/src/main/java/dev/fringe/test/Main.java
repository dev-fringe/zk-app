package dev.fringe.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

import dev.fringe.test.config.ZkCuratorConfig;
import lombok.SneakyThrows;
import lombok.val;

@Import(ZkCuratorConfig.class)
public class Main implements InitializingBean, DisposableBean {

	@Autowired CuratorFramework curator;
	@Autowired CuratorFramework curatorRetry;
	

	public static void main(String[] args){
		val context = new AnnotationConfigApplicationContext(Main.class);
		context.close();
	}

	@SneakyThrows
	public void create(String path, String data) {
		val stat = curator.checkExists().forPath(path);
		if (stat == null) {
			val message = curator.create().forPath(path, data.getBytes());
			System.out.println(message);
		}
	}

	@SneakyThrows
	public void update(String path, String data) {
		val datas = curator.getData().forPath(path);
		System.out.println(new String(datas));
		val stat = curator.setData().forPath(path, data.getBytes());
		System.out.println(stat);
	}

	@SneakyThrows
	public void delete(String path) {
		val stat = curator.checkExists().forPath(path);
		if (stat != null) {
			val datas = curator.getData().forPath(path);
			System.out.println("deleteNode : " + stat + ", data = " + new String(datas));
			curator.delete().deletingChildrenIfNeeded().forPath(path);
		}
	}
	
	@SneakyThrows
	private void nodesList(String path) {
		val paths = curator.getChildren().forPath(path);
		for (val p : paths) {
			System.out.println(p);
		}
	}
	
	public void afterPropertiesSet() {
		this.create("/data/path1", "new");
		this.nodesList("/data");
		this.update("/data/path1", "up");
		this.serverInfo();
	}

	private void serverInfo() {
        try {
            ServiceInstance<Object> instance = ServiceInstance.builder().name("myserver").address("192.168.1.106").port(8080).build();
            ServiceDiscovery<Object> serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class).client(curatorRetry).basePath("/").build();
            serviceDiscovery.registerService(instance);
            serviceDiscovery.start();
            System.out.println();
        } catch (Exception e) {
        	e.printStackTrace();
        }		
	}

	public void destroy() throws Exception {
		this.delete("/data/path1");
	}
	
}

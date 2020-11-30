package dev.fringe.test.scheduled;

import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.fringe.test.service.ZkDiscoveryService;
import lombok.SneakyThrows;
import lombok.val;

@Component
public class ZkRegisterJob {

	@Value("${server.port:8080}") int port; 
	@Value("${server.name:serviceA}") String serverName;
	@Autowired ZkDiscoveryService zkDiscoveryService;
	
	@SneakyThrows
	@PostConstruct
	public void registerService() {	
		val payload = String.valueOf(new Random().nextGaussian() + 10);
		val instance = extracted(serverName, InetAddress.getLocalHost().getHostAddress(), port, payload);
		zkDiscoveryService.registerService(instance);
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> updateService(instance.getId()), 5, 30, TimeUnit.SECONDS);
	}

	@SneakyThrows
	private ServiceInstance<String> extracted(String serverName, String address, int port, String payload) {
		return ServiceInstance.<String>builder().name(serverName).payload(payload).address(address).port(port).uriSpec(new UriSpec("http://" + address + ":" + port)).build();
	}
	
	@SneakyThrows
	protected void updateService(String id) {
		val payload = String.valueOf(new Random().nextGaussian() + 10);
		val updatedInstance =  zkDiscoveryService.updateInstancePayloadByServiceNameAndId(serverName, id, payload);
		zkDiscoveryService.updateService(updatedInstance);
	}
}

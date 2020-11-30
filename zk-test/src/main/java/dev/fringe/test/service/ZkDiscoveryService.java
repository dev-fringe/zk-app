package dev.fringe.test.service;

import java.util.ArrayList;

import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.val;

@Service
public class ZkDiscoveryService {

	@Autowired ServiceDiscovery<String> serviceDiscovery;
	
    public String findService(String serviceName) throws Exception{
    	val all = serviceDiscovery.queryForInstances(serviceName);
        if(all.size() == 0){
            return null;
        } else {
            return new ArrayList<>(all).get(0).getUriSpec().build();
        }
    }
    
    public ServiceInstance<String> findService(String serverName, String id) throws Exception{
    	return serviceDiscovery.queryForInstance(serverName, id);
    }
    
    public void registerService(ServiceInstance<String> serviceInstance) throws Exception {
        serviceDiscovery.registerService(serviceInstance);
    }

    public void unregisterService(ServiceInstance<String> serviceInstance) throws Exception {
        serviceDiscovery.unregisterService(serviceInstance);
    }

    public void updateService(ServiceInstance<String> serviceInstance) throws Exception {
        serviceDiscovery.updateService(serviceInstance);
    }

    @SneakyThrows
	public ServiceInstance<String> updateInstancePayloadByServiceNameAndId(String serverName, String id, String payload) {
    	val instance = this.findService(serverName, id);
		return ServiceInstance.<String> builder().id(instance.getId()).address(instance.getAddress()).payload(payload).name(instance.getName()).port(instance.getPort()).uriSpec(instance.getUriSpec()).build();
	}
}

package dev.fringe.test.service;

import java.util.ArrayList;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.val;

@Service
public class ZkDiscoveryService {

	@Autowired ServiceDiscovery<String> serviceDiscovery;
	
    public ServiceInstance<String> findService(String serviceName) throws Exception{
    	val all = serviceDiscovery.queryForInstances(serviceName);
        if(all.size() == 0){
            return null;
        } else {
            return new ArrayList<>(all).get(0);
        }
    }
}

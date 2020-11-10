package com.repositories.investigacion.v3.utilities.processing;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



import com.repositories.investigacion.v3.api.ServiceRegistry;
import com.repositories.investigacion.v3.utilities.dto.Entry;
import com.repositories.investigacion.v3.utilities.pojo.Repository;
import com.repositories.investigacion.v3.utilities.pojo.SynchronizedCache; 

public class ServiceThread extends Thread {
	
	public String name;
	
	public String query;
	
	public SynchronizedCache cache;
	
	public ServiceRegistry repoServices;
	
	public Map<String, Repository>  propertiesConfig;
	
	public void initialize(String sname, String squery, SynchronizedCache cache_queue, ServiceRegistry services, Map<String, Repository>  proper) {
		query = squery;
		name = sname;
		cache = cache_queue;
		repoServices = services;
		propertiesConfig = proper;
	}
	
	@Override
    public void run() {
		List<Entry> tempEntry = null;

		if(propertiesConfig.get(name).isStatus()) {
				String urlTemp = propertiesConfig.get(name).getUrl() + propertiesConfig.get(name).getId() + 
								"?apiKey=" + propertiesConfig.get(name).getKey() +
								"&query=all("+query.trim()+")&" +
								"count="+propertiesConfig.get(name).getCount()+"&"+
								"sort="+propertiesConfig.get(name).getSort(); 
				
			    urlTemp = urlTemp.replace(" ", "%20");
				
				System.out.println("name: " + name);
				System.out.println(repoServices.getHasMap().get(name));
				System.out.println("urlTemp: " + urlTemp);
				
				tempEntry = repoServices.getHasMap().get(name).getDataFrom(urlTemp); 	
			cache.add(new ResponseEntity<List<Entry>>(tempEntry,HttpStatus.OK));
		}
    }
}
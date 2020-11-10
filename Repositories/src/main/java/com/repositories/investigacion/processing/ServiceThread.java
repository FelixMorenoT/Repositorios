package com.repositories.investigacion.processing;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.repositories.investigacion.dto.Entry1;
import com.repositories.investigacion.rest.ServiceRegistry1;
import com.repositories.investigacion.utilities.PropertiesConfig; 

@Component
public class ServiceThread extends Thread {
	
	public String name;
	
	public String query;
	
	public SynchronizedCache cache;
	
	public ServiceRegistry1 repoServices;
	
	public PropertiesConfig propertiesConfig;
	
	public void initialize(String sname, String squery, SynchronizedCache cache_queue, ServiceRegistry1 services, PropertiesConfig proper) {
		query = squery;
		name = sname;
		cache = cache_queue;
		repoServices = services;
		propertiesConfig = proper;
	}
	
	@Override
    public void run() {
		List<Entry1> tempEntry = null;
		
		String urlTemp = propertiesConfig.getListProperties().get(name).getUrl() + name + 
						"?apiKey=" + propertiesConfig.getListProperties().get(name).getKey() +
						"&query=all("+query.trim()+")&" +
						"count="+propertiesConfig.getListProperties().get(name).getCount()+"&"+
						"sort="+propertiesConfig.getListProperties().get(name).getSort(); 
		
	    urlTemp = urlTemp.replace(" ", "%20");
		
		System.out.println("name: " + name);
		System.out.println(repoServices.getHasMap().get(name));
		System.out.println("urlTemp: " + urlTemp);
		
		tempEntry = repoServices.getHasMap().get(name).getDataFrom(urlTemp); 
		
		cache.add(new ResponseEntity<List<Entry1>>(tempEntry,HttpStatus.OK));
    }
}
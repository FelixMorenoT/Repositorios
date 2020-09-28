package com.repositories.investigacion.processing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component; 

import com.repositories.investigacion.rest.ServiceRegistry;

import java.util.List;


import com.repositories.investigacion.dto.Entry; 

@Component
public class ServiceThread extends Thread {
	
	public String name;
	
	public String query;
	
	public SynchronizedCache cache;
	
	public ServiceRegistry repoServices;
	
		
	@Value("${url.repository}")
	private String urlRepository = "https://api.elsevier.com/content/search/";
	
	@Value("${api.key}")
	private String apiKey="1d864054ddd712f5834da3fc841e4b1d";
	
	@Value("${search.count}")
	private String searchCount="10";
	
	@Value("${search.sort}")
	private String searchRelevancy="relevancy";
	
	
	public void initialize(String sname, String squery, SynchronizedCache cache_queue, ServiceRegistry services) {
		query = squery;
		name = sname;
		cache = cache_queue;
		repoServices = services;
	}
    
	@Override
    public void run() {
		List<Entry> tempEntry = null;
		String urlTemp = urlRepository + name + "?apiKey=" + apiKey +"&query=all("+query.trim()+")&count="+searchCount+"&sort="+searchRelevancy; 
	    urlTemp = urlTemp.replace(" ", "%20");
		
		System.out.println("name: " + name);
		System.out.println(repoServices.getHasMap().get(name));
		System.out.println("urlTemp: " + urlTemp);
		
		tempEntry = repoServices.getHasMap().get(name).getDataFrom(urlTemp); 
		
		cache.add(new ResponseEntity<List<Entry>>(tempEntry,HttpStatus.OK));
    }
}
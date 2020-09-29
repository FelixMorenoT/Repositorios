package com.repositories.investigacion.processing;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.repositories.investigacion.rest.ServiceRegistry;
import com.repositories.investigacion.services.imp.Connection;
import com.repositories.investigacion.services.imp.GenericService;
import com.repositories.investigacion.utilities.PropertiesConfig;
import com.repositories.investigacion.utilities.Repository;

public class ThreadInvoker {
	
	SynchronizedCache cache = new SynchronizedCache();
	ServiceRegistry repoServices;
	String query;
	PropertiesConfig properties;
	Connection connection = new Connection();

    public void initialize(String squery, ServiceRegistry services, PropertiesConfig proper) {
    	repoServices = services;
    	proper.setListProperties(heartBeat(proper.getListProperties()));
    	query = squery;
    	properties = proper;
	}
    
    public static String mapToString(Map<String, GenericService> map) {
	   StringBuilder stringBuilder = new StringBuilder();
	
	   for (String key : map.keySet()) {
	    if (stringBuilder.length() > 0) {
	     stringBuilder.append("&");
	    }
	    
	    try {
	     stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
	    } catch (UnsupportedEncodingException e) {
	     throw new RuntimeException("This method requires UTF-8 encoding support", e);
	    }
	   }
	
	   return stringBuilder.toString();
	}
    
	public SynchronizedCache invoke() {
        ExecutorService es = Executors.newFixedThreadPool(4);
        List<Runnable> tasks = new ArrayList<Runnable>();

 	    for (String key : repoServices.getHasMap().keySet()) {
	 	    try {
	 	    	if(key != null) {
	 	    		ServiceThread t = new ServiceThread();
	 	    		t.initialize(URLEncoder.encode(key, "UTF-8"), query, cache, repoServices, properties);
	 	    		tasks.add(t);
	 	    	}
	 	    } catch (UnsupportedEncodingException e) {
	 	     throw new RuntimeException("This method requires UTF-8 encoding support", e);
	 	    }
 	    }
 	    
        CompletableFuture<?>[] futures = tasks.stream().map(task -> CompletableFuture.runAsync(task, es)).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        es.shutdown();
        System.out.println("FIN DEL PROCESO DE HILOS");
        
        return cache;
    }
	
	private HashMap<String, Repository> heartBeat(HashMap<String, Repository> tempRepos){

		for (Entry<String, Repository> entry : tempRepos.entrySet()) {
		    String key = entry.getKey();
		    Repository value = entry.getValue();
		    System.out.println("HB - " + value.getUrl() + value.getId() + "/?apiKey=" + value.getKey() +"&query=all(test)&count=1");
		    boolean resultHeartBeat = connection.heartBeat(value.getUrl() + value.getId() + "/?apiKey=" + value.getKey() +"&query=all(test)&count=1");
		    
		    if(!resultHeartBeat) {
		    	tempRepos.remove(key);
		    	this.repoServices.getHasMap().remove(key);
		    }
		}

		return tempRepos;
	}
}
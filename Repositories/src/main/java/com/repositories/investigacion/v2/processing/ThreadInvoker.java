package com.repositories.investigacion.v2.processing;

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

import com.repositories.investigacion.v2.rest.ServiceRegistry1;
import com.repositories.investigacion.v2.utilities.PropertiesConfig;
import com.repositories.investigacion.v2.utilities.Repository;
import com.repositories.investigacion.v3.api.layer.GenericService;
import com.repositories.investigacion.v3.connection.ConnectionService;

public class ThreadInvoker {
	
	SynchronizedCache cache = new SynchronizedCache();
	ServiceRegistry1 repoServices;
	String query;
	PropertiesConfig properties;
	ConnectionService connection = new ConnectionService();

    public void initialize(String squery, ServiceRegistry1 services, PropertiesConfig proper) {
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
	
	private HashMap<String, com.repositories.investigacion.v3.utilities.pojo.Repository> heartBeat(HashMap<String, com.repositories.investigacion.v3.utilities.pojo.Repository> tempRepos){

		for (Entry<String, com.repositories.investigacion.v3.utilities.pojo.Repository> entry : tempRepos.entrySet()) {
		    String key = entry.getKey();
		    com.repositories.investigacion.v3.utilities.pojo.Repository value = entry.getValue();
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
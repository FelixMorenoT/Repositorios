package com.repositories.investigacion.processing;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import com.repositories.investigacion.rest.ServiceRegistry;
import com.repositories.investigacion.services.GenericService;

@Component
public class ThreadInvoker {
	
	SynchronizedCache cache = new SynchronizedCache();
	ServiceRegistry repoServices;
	String query;

    public void initialize(String squery, ServiceRegistry services) {
    	repoServices = services;
    	query = squery;
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
	 	    		t.initialize(URLEncoder.encode(key, "UTF-8"), query, cache, repoServices);
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
}
package com.repositories.investigacion.rest;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.repositories.investigacion.services.imp.GenericService;
import com.repositories.investigacion.services.imp.ScienceDirectServices;
import com.repositories.investigacion.services.imp.ScopusServices;

@Component
public class ServiceRegistry{

	private HashMap<String, GenericService> servicesR = new HashMap<String, GenericService>();
	
	@Autowired
	private ScopusServices scopusServices;
	
	@Autowired
	private ScienceDirectServices scienceDirectServices;

	public void init() {
		getHasMap().put("scopus", scopusServices);
		getHasMap().put("sciencedirect", scienceDirectServices);
	}
	
	public HashMap<String, GenericService> getHasMap() {
		return servicesR;
	}
	
	public String getStringMap() {
	    StringBuilder mapAsString = new StringBuilder();
	    for (String key : getHasMap().keySet()) {
	        mapAsString.append(key + "=" + servicesR.get(key) + ", ");
	    }
	    return mapAsString.toString();
	}

}

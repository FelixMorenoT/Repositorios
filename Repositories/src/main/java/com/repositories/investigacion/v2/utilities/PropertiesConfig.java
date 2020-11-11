package com.repositories.investigacion.v2.utilities;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Service
@RefreshScope
public class PropertiesConfig {

	
	@Autowired
	private PropertiesReader propertiesReader;
	
	private HashMap<String, Repository> listProperties;

	@PostConstruct
	public void loadProperties () {
		listProperties = propertiesReader.loadProperties();
		//listProperties.forEach((k,v) -> System.out.println("Key: " + k + ": Value: " + v.toString()));
	}
	
	public HashMap<String, Repository> getListProperties() {
		return listProperties;
	}

	public void setListProperties(HashMap<String, Repository> listProperties) {
		this.listProperties = listProperties;
	}
	
	public void reload() {
		loadProperties();
	}
}

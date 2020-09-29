package com.repositories.investigacion.services.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.repositories.investigacion.dto.Entry;
import com.repositories.investigacion.services.ServicesRepositories;

@Service
public class GenericService implements ServicesRepositories{
	
	@Autowired
	private Connection conn;
	
	private String name;

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public List<Entry> getDataFrom(String theUrl) {
		return null;
	}


}

package com.repositories.investigacion.v3.api.layer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.repositories.investigacion.v3.connection.ConnectionService;
import com.repositories.investigacion.v3.utilities.dto.Entry;

@Service
public class GenericService implements IGenericService{
	
	@Autowired
	private ConnectionService conn;
	
	private String name;

	public ConnectionService getConn() {
		return conn;
	}

	public void setConn(ConnectionService conn) {
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

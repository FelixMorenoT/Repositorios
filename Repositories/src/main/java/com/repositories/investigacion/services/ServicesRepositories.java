package com.repositories.investigacion.services;

import java.util.List;

import com.repositories.investigacion.dto.Entry;

public interface ServicesRepositories {

	public List<Entry> getDataFrom(String theUrl);
}

package com.repositories.investigacion.services;

import java.util.List;

import com.repositories.investigacion.v3.utilities.dto.Entry;

public interface ServicesRepositories {

	public List<Entry> getDataFrom(String theUrl);
}

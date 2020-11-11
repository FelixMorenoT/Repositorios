package com.repositories.investigacion.v3.api.layer;

import java.util.List;

import com.repositories.investigacion.v3.utilities.dto.Entry;

public interface IGenericService {

	public List<Entry> getDataFrom(String theUrl);
}

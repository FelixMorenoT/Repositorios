package com.repositories.investigacion.v3.loader;

import java.util.Map;

import com.repositories.investigacion.v3.utilities.pojo.Repository;

public interface IPropertieLoader {

	public void loader();
	public Map<String, Repository> getProperties();
	public void reload();
}

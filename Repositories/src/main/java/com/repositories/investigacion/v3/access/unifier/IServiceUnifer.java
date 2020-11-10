package com.repositories.investigacion.v3.access.unifier;

import java.util.List;


import com.repositories.investigacion.v3.utilities.dto.Entry;
import com.repositories.investigacion.v3.utilities.pojo.SynchronizedCache;

public interface IServiceUnifer {

	public List<Entry> unifier(SynchronizedCache cache);
}

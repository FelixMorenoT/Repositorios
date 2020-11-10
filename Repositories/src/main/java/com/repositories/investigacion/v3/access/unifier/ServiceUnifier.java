package com.repositories.investigacion.v3.access.unifier;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.repositories.investigacion.v3.utilities.dto.Entry;
import com.repositories.investigacion.v3.utilities.pojo.SynchronizedCache;

@Service
public class ServiceUnifier implements IServiceUnifer {

	@Override
	public List<Entry> unifier(SynchronizedCache cache) {
		List<Entry> tempResult = new ArrayList<Entry>();
		
		for (int i = 0; i < cache.getCache().size(); i++) {
			List<Entry> temp = cache.get(i).getBody();
			
			for (Entry value : temp) {
				tempResult.add(value);
			}
		}
		
		return tempResult;
	}

}

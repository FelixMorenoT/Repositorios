package com.repositories.investigacion.v3.access.unifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
		
		
		List<Entry> result = tempResult.stream().sorted((o1, o2)->o1.getAuthor().
                compareTo(o2.getAuthor())).
                collect(Collectors.toList());
		
		return result;
	}

}

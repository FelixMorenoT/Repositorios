package com.repositories.investigacion.utilities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.repositories.investigacion.dto.Entry1;
import com.repositories.investigacion.processing.SynchronizedCache;

@Service
public class SyncResult {

	public List<Entry1> getSyncResult(SynchronizedCache synchronizedCache){
		List<Entry1> tempResult = new ArrayList<Entry1>();
		
		for (int i = 0; i < synchronizedCache.getCache().size(); i++) {
			List<Entry1> temp = synchronizedCache.get(i).getBody();
			
			for (Entry1 value : temp) {
				tempResult.add(value);
			}
		}
		
		return tempResult;
	}
}

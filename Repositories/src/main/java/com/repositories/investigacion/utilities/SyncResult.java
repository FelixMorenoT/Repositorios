package com.repositories.investigacion.utilities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.repositories.investigacion.dto.Entry;
import com.repositories.investigacion.processing.SynchronizedCache;

@Service
public class SyncResult {

	public List<Entry> getSyncResult(SynchronizedCache synchronizedCache){
		List<Entry> tempResult = new ArrayList<Entry>();
		
		for (int i = 0; i < synchronizedCache.getCache().size(); i++) {
			List<Entry> temp = synchronizedCache.get(i).getBody();
			
			for (Entry value : temp) {
				tempResult.add(value);
			}
		}
		
		return tempResult;
	}
}

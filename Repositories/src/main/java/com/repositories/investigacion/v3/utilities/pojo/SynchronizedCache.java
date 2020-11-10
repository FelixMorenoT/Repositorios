package com.repositories.investigacion.v3.utilities.pojo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.repositories.investigacion.v3.utilities.dto.Entry;

public class SynchronizedCache {
	private ArrayList<ResponseEntity<List<Entry>>> cache = new ArrayList<ResponseEntity<List<Entry>>>();
	
	public synchronized void add(ResponseEntity<List<Entry>> responseEntity){
		cache.add(responseEntity);
	}

	public void print() {
		System.out.println("Final size:" + cache.size());
	}
	
	public ArrayList<ResponseEntity<List<Entry>>> getCache(){
		return cache;	
	}
	
	public ResponseEntity<List<Entry>> get(int i) {
		return cache.get(i);
	}
}

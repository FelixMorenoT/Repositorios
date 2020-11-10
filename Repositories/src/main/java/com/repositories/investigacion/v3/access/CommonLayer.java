package com.repositories.investigacion.v3.access;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repositories.investigacion.v3.access.invoker.IServiceInvoker;
import com.repositories.investigacion.v3.access.unifier.IServiceUnifer;
import com.repositories.investigacion.v3.utilities.dto.Entry;
import com.repositories.investigacion.v3.utilities.pojo.SynchronizedCache;


@RestController
@RequestMapping("/api/v3")
public class CommonLayer implements ICommonLayer{
 
	private static final Logger log = LoggerFactory.getLogger(CommonLayer.class);

	@Autowired
	private IServiceInvoker servicesInvoker;
	
	@Autowired
	private IServiceUnifer resultUnifer;

	@Override
	@GetMapping("/general/{query}")
	public ResponseEntity<List<Entry>> accessRepoService(@PathVariable("query") String query) {
		log.info("Query to execute: {}" , query);
		
		SynchronizedCache result =servicesInvoker.resultInvoker(query);
		List<Entry> articlesResults = resultUnifer.unifier(result);
		return new ResponseEntity<List<Entry>>(articlesResults,HttpStatus.OK);
	}
	

	
	
	

}

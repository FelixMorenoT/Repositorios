package com.repositories.investigacion.rest;

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

import com.repositories.investigacion.dto.Entry;
import com.repositories.investigacion.processing.SynchronizedCache;
import com.repositories.investigacion.processing.ThreadInvoker;
import com.repositories.investigacion.utilities.PropertiesConfig;
import com.repositories.investigacion.utilities.SyncResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/apiv2")
@Api
public class RestAccess {

	private static final Logger log = LoggerFactory.getLogger(RestAccess.class);
	
	@Autowired
	private ServiceRegistry repoServices;
	
	@Autowired
	private SyncResult syncResult;
	
	@Autowired
	private PropertiesConfig propertiesConfig;

	@GetMapping("/{repository}/{keyWords}")
	public ResponseEntity<List<Entry>>  accessToService(@PathVariable("repository") String repository , @PathVariable("keyWords") String words) {
		log.info("Start " + repository);
		
		List<Entry> tempEntry = null;
		String urlTemp = propertiesConfig.getListProperties().get(repository).getUrl() + 
				repository + "?apiKey=" + propertiesConfig.getListProperties().get(repository).getKey() 
				+"&query=all("+words.trim()+")"
				+"&count="+propertiesConfig.getListProperties().get(repository).getCount()
				+"&sort="+propertiesConfig.getListProperties().get(repository).getSort(); 
	
		System.out.println(urlTemp);
		
		tempEntry = repoServices.getHasMap().get(repository).getDataFrom(urlTemp.replace(" ", "%20"));
		log.info("End " + repository);
		return new ResponseEntity<List<Entry>>(tempEntry,HttpStatus.OK);
	}
	
	@GetMapping("/general/{keyWords}")
	public ResponseEntity<List<Entry>> accessToGeneralService(@PathVariable("keyWords") String words) {
		repoServices.init();
		propertiesConfig.reload();
		ThreadInvoker threads = new ThreadInvoker();
		threads.initialize(words, repoServices, propertiesConfig);
		SynchronizedCache result = threads.invoke();
		List<Entry> listResult = syncResult.getSyncResult(result);
		return new ResponseEntity<List<Entry>>(listResult, HttpStatus.OK);
	}

}
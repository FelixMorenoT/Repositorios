package com.repositories.investigacion.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repositories.investigacion.dto.Entry;
import com.repositories.investigacion.services.ScienceDirectServices;
import com.repositories.investigacion.services.ScopusServices;

@RestController
@RequestMapping("/api")
public class RestAccess {

	private static final Logger log = LoggerFactory.getLogger(RestAccess.class);
	
	@Autowired
	private ScopusServices scopusServices;
	
	@Autowired
	private ScienceDirectServices scienceDirectServices;
	
	@Value("${url.repository}")
	private String urlRepository;
	
	@Value("${api.key}")
	private String apiKey;
	
	@Value("${search.count}")
	private String searchCount;
	
	@Value("${serach.sort}")
	private String searchRelevancy;
	
	@GetMapping("/{repository}/{keyWords}")
	public ResponseEntity<List<Entry>>  accessToScopus(@PathVariable("repository") String repository , @PathVariable("keyWords") String words) {
		log.info("Start " + repository);
		
			List<Entry> tempEntry = null;
			String urlTemp = urlRepository + repository + "?apiKey=" + apiKey +"&query=all("+words.trim()+")&count="+searchCount+"&sort="+searchRelevancy; 
		
			System.out.println(urlTemp);

			switch (repository.toLowerCase()) {
				case "scopus":
					tempEntry = scopusServices.getDataFrom(urlTemp.replace(" ", "%20"));
					log.info("End " + repository);
					return new ResponseEntity<List<Entry>>(tempEntry,HttpStatus.OK);
			
				case "sciencedirect":
					tempEntry = scienceDirectServices.getDataFrom(urlTemp.replace(" ", "%20"));
					log.info("End " + repository);
					return new ResponseEntity<List<Entry>>(tempEntry,HttpStatus.OK);

			default:
				break;
			}
		log.info("End" + repository);
		return new ResponseEntity<List<Entry>>(tempEntry,HttpStatus.NOT_FOUND);
	}
}

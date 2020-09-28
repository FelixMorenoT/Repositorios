package com.repositories.investigacion.utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertiesConfig {

	@Value("${url.repository}")
	private String urlRepository ;
	
	@Value("${api.key}")
	private String apiKey;
	
	@Value("${search.count}")
	private String searchCount;
	
	@Value("${search.sort}")
	private String searchRelevancy;

	public String getUrlRepository() {
		return urlRepository;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getSearchCount() {
		return searchCount;
	}

	public String getSearchRelevancy() {
		return searchRelevancy;
	}
}

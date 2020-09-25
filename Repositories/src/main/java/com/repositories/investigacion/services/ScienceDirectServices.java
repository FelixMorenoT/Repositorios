package com.repositories.investigacion.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.repositories.investigacion.dto.Entry;

@Service
public class ScienceDirectServices implements ServicesRepositories {
	
	@Autowired
	private Connection conn;
	
	@Override
	public List<Entry> getDataFrom(String theUrl) {
		StringBuffer tempBuffer = conn.getDataRaw(theUrl);
		String tempStrAuthors = "";
				
		List<Entry> tempArray = new ArrayList<Entry>();
		Entry entryTemp = null;
		
		JsonObject jsonRawDataObject = (JsonObject) JsonParser.parseString(tempBuffer.toString());
		JsonObject jsonDataObject = (JsonObject) jsonRawDataObject.get("search-results");
		JsonArray jsonEntryArry = (JsonArray) jsonDataObject.get("entry");
		
		for (JsonElement jsonElement : jsonEntryArry) {
			tempStrAuthors = "";
			entryTemp = new Entry();
			entryTemp.setIdentifier(jsonElement.getAsJsonObject().get("dc:identifier").toString().replace("\"", ""));
			entryTemp.setTitle(jsonElement.getAsJsonObject().get("dc:title").toString().replace("\"", ""));
			JsonObject tempAuthorsOne;
			
				if(jsonElement.getAsJsonObject().get("authors") instanceof JsonNull){
					tempStrAuthors = "NA";
					entryTemp.setAuthor(tempStrAuthors);
				}else {
					tempAuthorsOne = (JsonObject) jsonElement.getAsJsonObject().get("authors");
					if(tempAuthorsOne.toString().contains("$")) {
						JsonArray tempAuthorsTwo = (JsonArray) tempAuthorsOne.get("author");
						for (JsonElement jsonElement2 : tempAuthorsTwo) {
							tempStrAuthors += jsonElement2.getAsJsonObject().get("$").toString().replace("\"", "") + "; ";
						}
						entryTemp.setAuthor(tempStrAuthors.substring(0, tempStrAuthors.length()-2));
					}else {
						entryTemp.setAuthor(tempAuthorsOne.get("author").toString().replace("\"", ""));
					}
				}
									
			entryTemp.setDate(jsonElement.getAsJsonObject().get("prism:coverDate").toString().replace("\"", ""));
			entryTemp.setHref(jsonElement.getAsJsonObject().get("link").getAsJsonArray().get(1).getAsJsonObject().get("@href").toString().replace("\"", ""));
			tempArray.add(entryTemp);
		}
	
		return tempArray;
	}

}

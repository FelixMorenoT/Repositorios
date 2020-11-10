package com.repositories.investigacion.services.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.repositories.investigacion.v3.utilities.dto.Entry;

@Service
public class ScopusServices extends GenericService {

	public List<Entry> getDataFrom(String theUrl) {
		StringBuffer tempBuffer = this.getConn().getDataRaw(theUrl);
		
		List<Entry> tempArray = new ArrayList<Entry>();
		Entry entryTemp = null;
		
		JsonObject jsonRawDataObject = (JsonObject) JsonParser.parseString(tempBuffer.toString());
		JsonObject jsonDataObject = (JsonObject) jsonRawDataObject.get("search-results");
		JsonArray jsonEntryArry = (JsonArray) jsonDataObject.get("entry");
		
		for (JsonElement jsonElement : jsonEntryArry) {
			entryTemp = new Entry();
			entryTemp.setIdentifier(jsonElement.getAsJsonObject().get("dc:identifier").toString().replace("\"", ""));
			entryTemp.setTitle(jsonElement.getAsJsonObject().get("dc:title").toString().replace("\"", ""));
			entryTemp.setAuthor(jsonElement.getAsJsonObject().get("dc:creator").toString().replace("\"", ""));
			entryTemp.setDate(jsonElement.getAsJsonObject().get("prism:coverDisplayDate").toString().replace("\"", ""));
			entryTemp.setHref(jsonElement.getAsJsonObject().get("link").getAsJsonArray().get(2).getAsJsonObject().get("@href").toString().replace("\"", ""));
			tempArray.add(entryTemp);
		}
	
		return tempArray;
	}
}

package com.repositories.investigacion.v2.utilities;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class PropertiesReader {

	@Value("${config.path}")
	private String configPath;
	
	public HashMap<String, Repository> loadProperties() {
		HashMap<String, Repository> tempProperties  = new HashMap<String, Repository>();
		
		try {
			File file = new File(configPath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
			DocumentBuilder db = dbf.newDocumentBuilder();  
			Document doc = db.parse(file);  
			doc.getDocumentElement().normalize();  
		
			NodeList nodeList = doc.getElementsByTagName("Repository");  
			
			for (int itr = 0; itr < nodeList.getLength(); itr++){ 
				Node node = nodeList.item(itr);  
	
				if (node.getNodeType() == Node.ELEMENT_NODE)   {  
					Element eElement = (Element) node;  
					Repository repo = new Repository();
						repo.setId(eElement.getElementsByTagName("id").item(0).getTextContent());
						repo.setUrl(eElement.getElementsByTagName("url").item(0).getTextContent());
						repo.setKey(eElement.getElementsByTagName("key").item(0).getTextContent());
						repo.setCount(eElement.getElementsByTagName("count").item(0).getTextContent());
						repo.setSort(eElement.getElementsByTagName("sort").item(0).getTextContent());
					tempProperties.put(repo.getId(), repo);
				} 
			}
			return tempProperties;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}  
		
	}

}

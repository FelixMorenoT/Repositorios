package com.repositories.investigacion.v3.loader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.repositories.investigacion.v3.utilities.pojo.Repository;

@Component
public class PropertieLoader implements  IPropertieLoader {

	private static final Logger log = LoggerFactory.getLogger(PropertieLoader.class);
	
	@Value("${config.path}")
	private String configPath;
	
	private Map<String, Repository> hashProperties  = new HashMap<String, Repository>();;
	
	@PostConstruct
	@Override
	public void loader() {
		log.info("Loading Properties");
		loaderMethod();
	}
	
	private void loaderMethod() {
		 
		try {
			File file = new File(configPath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
			DocumentBuilder db = dbf.newDocumentBuilder();  
			Document doc = db.parse(file);  
			doc.getDocumentElement().normalize();  
		
			NodeList nodeList = doc.getElementsByTagName("Repository");  
			
			log.info("Number Properties XML: {} " , nodeList.getLength());
			log.info("Number Properties Memory: {} " , hashProperties.size());
			
			switch (hashProperties.size()) {
			case 0:
					log.info("Initial Properties");
					extractXMLData(nodeList);
				break;
		
			default:
				if(hashProperties.size() < nodeList.getLength()) {
					log.info("Update Properties");
					extractXMLData(nodeList);
				}
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	private void extractXMLData(NodeList nodeList){
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
					repo.setStatus(Boolean.valueOf(eElement.getElementsByTagName("online").item(0).getTextContent()));
					hashProperties.put(repo.getId(), repo);
			} 
		}
	}
	public Map<String, Repository> getHashProperties() {
		return hashProperties;
	}

	@Override
	public Map<String, Repository> getProperties() {
		return getHashProperties();
	}
}

package com.repositories.investigacion.v3.loader;

import java.io.InputStream;
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

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.repositories.investigacion.v3.utilities.pojo.Repository;

@Component
public class PropertieLoader implements  IPropertieLoader {

	private static final Logger log = LoggerFactory.getLogger(PropertieLoader.class);
	
	@Value("${config.path}")
	private String configPath;
	
	private Map<String, Repository> hashProperties  = new HashMap<String, Repository>();
	
	private Regions clientRegion = Regions.US_EAST_2;
	private String bucketName = "propertiesrepo";
	private String key = "Properties.xml";
	private S3Object fullObject = null;

	
	@PostConstruct
	@Override
	public void loader() {
		log.info("Loading Properties");
		loaderMethod();
	}
	
	@Override
	public void reload() {
		log.info("Reloading Properties");
		loaderMethod();
	}
	
	private void loaderMethod() {
		try {  
			
			BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAISO5EUNY7CGMBLVA", "7/FCsBZF9iXJZbDPFjH5ozoV/hk5IfmJnzNR6Ry4");
	
	        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
	                    .withRegion(clientRegion)
	                    .build();
	        
	        fullObject = s3Client.getObject(new GetObjectRequest(bucketName, key));
            
            InputStream is =fullObject.getObjectContent();
	        
			//File file = new File(configPath);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
			DocumentBuilder db = dbf.newDocumentBuilder();  
			Document doc = db.parse(is);  
			doc.getDocumentElement().normalize();  
		
			NodeList nodeList = doc.getElementsByTagName("Repository");  
			
			log.info("Number Properties XML: {} " , nodeList.getLength());
			log.info("Number Properties Memory: {} " , hashProperties.size());
			 
			switch (hashProperties.size()) {
			case 0:
					log.info("Initial Properties");
					extractXMLData(nodeList,false);
				break;
		
			default:
				if(hashProperties.size() <= nodeList.getLength()) {
					log.info("Update Properties");
					extractXMLData(nodeList,true);
				}
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void extractXMLData(NodeList nodeList, boolean flag){
		
		if(flag) {
			for (int itr = 0; itr < nodeList.getLength(); itr++){ 
				Node node = nodeList.item(itr);  
				if (node.getNodeType() == Node.ELEMENT_NODE)   { 
					Element eElement = (Element) node;  
					
					Repository repo = hashProperties.get(eElement.getElementsByTagName("id").item(0).getTextContent());
						repo.setUrl(eElement.getElementsByTagName("url").item(0).getTextContent());
						repo.setKey(eElement.getElementsByTagName("key").item(0).getTextContent());
						repo.setCount(eElement.getElementsByTagName("count").item(0).getTextContent());
						repo.setSort(eElement.getElementsByTagName("sort").item(0).getTextContent());
					hashProperties.computeIfPresent(eElement.getElementsByTagName("id").item(0).getTextContent(), (k,v)-> repo);
				} 
			}
		}else {
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
	}
	public Map<String, Repository> getHashProperties() {
		return hashProperties;
	}

	@Override
	public Map<String, Repository> getProperties() {
		return getHashProperties();
	}
}

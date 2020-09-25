package com.repositories.investigacion.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

@Service
public class Connection {
	
	private StringBuffer buffer = null;
	
	public StringBuffer getDataRaw(String url) {
       try {
            URL theUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) theUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
           	BufferedReader br = new BufferedReader(in);
            String output="";
            buffer = new StringBuffer();
            while ((output = br.readLine()) != null) {
            	buffer.append(output);
            }

            return buffer;

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
		return null;
	}
}

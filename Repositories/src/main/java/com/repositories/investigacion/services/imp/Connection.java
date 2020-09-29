package com.repositories.investigacion.services.imp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.repositories.investigacion.services.ConnectionService;

@Service
public class Connection implements ConnectionService {
	
	private StringBuffer buffer = null;
	
	public StringBuffer getDataRaw(String url) {
       try {
          
            HttpURLConnection conn = startConnection(url,false);

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
	
	public boolean heartBeat(String urlHost) {
		//urlHost = urlHost.replaceFirst("^https", "http"); 
		
        try {
            HttpURLConnection connection = startConnection(urlHost,true);
            int responseCode = 0;
            responseCode = connection.getResponseCode();
            System.out.println("Response Code " + responseCode);
            connection.disconnect();
            return (200 <= responseCode && responseCode <= 400);
        } catch (IOException exception) {
            return false;
        }
	}

	@Override
	public HttpURLConnection startConnection(String url, boolean flag) {
		
		try {
			URL theUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) theUrl.openConnection();
			if(flag) {
				conn.setConnectTimeout(4000);
				conn.setReadTimeout(4000);
				conn.setRequestMethod("HEAD");
				conn.setRequestProperty("Accept", "application/json");

			}else {
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
			}
			
			return conn;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

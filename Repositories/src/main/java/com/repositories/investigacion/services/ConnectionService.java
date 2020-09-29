package com.repositories.investigacion.services;

import java.net.HttpURLConnection;

public interface ConnectionService {

	public HttpURLConnection startConnection (String url, boolean flag) ;
}

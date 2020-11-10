package com.repositories.investigacion.v3.connection;

import java.net.HttpURLConnection;

public interface IConnectionService {

	public HttpURLConnection startConnection (String url, boolean flag) ;
}

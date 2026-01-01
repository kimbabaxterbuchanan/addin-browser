package com.android;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpParams;

public class MyHttpClient extends DefaultHttpClient {
	//This is the constructor
	public MyHttpClient()
	{
	}
	//use this constructor if parameters needs to be sent
	public MyHttpClient(ClientConnectionManager con,HttpParams param)
	{
		setParams(param);
	}
	//overriding the function to skip ssl certificate validation
	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		registry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
		return new SingleClientConnManager(getParams(), registry);
	}
}
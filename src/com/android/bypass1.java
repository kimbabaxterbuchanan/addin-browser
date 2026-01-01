package com.android;

import javax.net.ssl.HttpsURLConnection;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.net.http.*;
import android.webkit.SslErrorHandler;

public class bypass1
{
	public static WebViewClient byp()
	{
		return new WebViewClient(){
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError
					error){
				handler.proceed();
			}
		};
	}
	public static void webviewbypass(WebView view)
	{
		view.setWebViewClient(byp());
	}
}
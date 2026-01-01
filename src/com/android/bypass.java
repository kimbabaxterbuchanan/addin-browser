package com.android;

import javax.net.ssl.HttpsURLConnection;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.net.http.*;
import android.webkit.SslErrorHandler;

public class bypass
{
	public static void httpsurlconnectionbypass(HttpsURLConnection https)
	{
		https.setHostnameVerifier(httpsurlbypass.hostname());
	}
}
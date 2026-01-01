package com.example.addin_browser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.android.MyHttpClient;
import com.android.bypass;
import com.android.httpsurlbypass;


public class MainActivity extends Activity {

	WebView mWebView;
	String Response;

	@Override
	//	protected void onCreate(Bundle savedInstanceState) {
	//		super.onCreate(savedInstanceState);
	//		setContentView(R.layout.activity_main);
	//        mWebView = (WebView) findViewById(R.id.activity_main_webview);
	//     // Enable Javascript
	//        WebSettings webSettings = mWebView.getSettings();
	//        webSettings.setJavaScriptEnabled(true);
	//        mWebView.loadUrl("http://www.google.com/");
	//
	//
	//	}
	//
	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		// Inflate the menu; this adds items to the action bar if it is present.
	//		getMenuInflater().inflate(R.menu.main, menu);
	//		return true;
	//	}
	public void onCreate(Bundle savedInstanceState) {
		int cnt = 0;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Response = null;
		new RetreiveFeedTask().execute("https://sciis.website.com/addin_web/addin_RA_logon.aspx");
		while (Response == null )
		{
			if (cnt > 100)
			{ 
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cnt++;
		}
		mWebView = (WebView) findViewById(R.id.activity_main_webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl("https://sciis.website.com/addin_web/addin_RA_logon.aspx");
//		mWebView.loadData(Response, "text/html", "utf-8");
	}
	@SuppressLint("SetJavaScriptEnabled")
	private class RetreiveFeedTask extends AsyncTask<String, Void, String> {

		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */

		@Override
		protected String doInBackground(String... arg0) {
			String m = "";
			try {
//				mWebView = (WebView) findViewById(R.id.activity_main_webview);
//				mWebView.getSettings().setJavaScriptEnabled(true);
				DefaultHttpClient client = new MyHttpClient();
				HttpGet get = new HttpGet("https://sciis.website.com/addin_web/addin_RA_logon.aspx");
				HttpResponse getResponse = client.execute(get);
				Response = getResponse.toString();
				m = Response;
				if(getResponse.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
				{
//					mWebView.loadData("Success Traffic was routed via proxy tool", "text/html", "utf-8");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//				mWebView = (WebView) findViewById(R.id.activity_main_webview);
//				mWebView.loadData("Fail Traffic was not routed via proxy tool", "text/html", "utf-8");
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//				mWebView = (WebView) findViewById(R.id.activity_main_webview);
//				mWebView.loadData("Fail Traffic was not routed via proxy tool", "text/html", "utf-8");
			}
			return m;
		}
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
		}
	}
}



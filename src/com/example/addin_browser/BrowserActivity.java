package com.example.addin_browser;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.android.MyHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class BrowserActivity extends Activity {
    private String Response;
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		try {
//			mWebView = (WebView) findViewById(R.id.activity_main_webview);
//			mWebView.getSettings().setJavaScriptEnabled(true);
			DefaultHttpClient client = new MyHttpClient();
			HttpGet get = new HttpGet("https://raiis.website.com");
			HttpResponse getResponse = client.execute(get);
			Response = getResponse.toString();
			if(getResponse.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
			{
//				mWebView.loadData("Success Traffic was routed via proxy tool", "text/html", "utf-8");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			mWebView = (WebView) findViewById(R.id.activity_main_webview);
//			mWebView.loadData("Fail Traffic was not routed via proxy tool", "text/html", "utf-8");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			mWebView = (WebView) findViewById(R.id.activity_main_webview);
//			mWebView.loadData("Fail Traffic was not routed via proxy tool", "text/html", "utf-8");
		}
		executeDone();
    }

     /**
     *
     */
    private void executeDone() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("value", Response);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}

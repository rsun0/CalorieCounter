package com.mycompany.caloriecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Displays a website for searching calorie amounts in a WebView
 */
public class WebActivity extends AppCompatActivity {

    @Override
    /**
     * Launches the WebView
     * @param savedInstanceState The saved instance state
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(getString(R.string.url));
    }
}
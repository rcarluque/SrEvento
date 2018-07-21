package com.example.rafa.srevento;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.System.currentTimeMillis;

public class NavegadorActivity extends AppCompatActivity {
    WebView myWebView;
    String urlMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        urlMain = getIntent().getStringExtra("URL");

        myWebView = (WebView) this.findViewById(R.id.webView);

        // Enable JavaScript WebSettings webSettings
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Provide a WebViewClient for your WebView
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl(urlMain);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        // Check if there's history
        if (this.myWebView.canGoBack())
            this.myWebView.goBack();
        else
            super.onBackPressed();

    }

    private class MyWebViewClient extends WebViewClient {

        private long loadTime; // Web page loading time

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (Uri.parse(url).getHost().equals(urlMain)) {
                // This is my web site, so do not override; let my WebView load
                // the page
                return false;
            }

            // Otherwise, the link is not for a page on my site, so launch
            // another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            // Save start time
            this.loadTime = currentTimeMillis();

            // Show a toast
            Toast.makeText(getApplicationContext(),
                    "La página se ha empezado a cargar...", LENGTH_SHORT).show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            // Calculate load time
            this.loadTime = currentTimeMillis() - this.loadTime;

            // Convert milliseconds to date format
            String time = new SimpleDateFormat("mm:ss:SSS", Locale.getDefault())
                    .format(new Date(this.loadTime));

            // Show a toast
            Toast.makeText(getApplicationContext(),
                    "La página ha terminado de cargar en... " + time, LENGTH_SHORT)
                    .show();

        }
    }

}
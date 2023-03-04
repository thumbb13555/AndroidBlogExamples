package com.noahliu.webviewexample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private TextView tvStatus;
    private boolean isGoogle = true;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = findViewById(R.id.textView_Status);
        EditText edInput = findViewById(R.id.editText_Input);
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.google.com/");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //取得Cookies
                String cook = CookieManager.getInstance().getCookie(url);
                if (cook!= null){
                    String[] cookies = cook.split(";");
                    for (String cookie:cookies) {
                        Log.d("TAG", "onPageFinished: "+cookie);
                    }
                }
                String javascript;
                if (isGoogle){
                    javascript = "document.getElementsByName('q')[0].value = '如何脫單';";
                }else{
                    javascript = "document.getElementById('says').innerHTML = '"
                            +edInput.getText()+"';";
                }
                view.evaluateJavascript(javascript, null);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                tvStatus.setText("進度:\n"+newProgress);
            }
        });

        (findViewById(R.id.button_Google)).setOnClickListener(view -> {
            isGoogle = true;
            webView.loadUrl("loadUrl('about:blank')");
            webView.clearCache(true);
            webView.loadUrl("https://www.google.com/");
        });
        (findViewById(R.id.button_MyPage)).setOnClickListener(view -> {
            isGoogle = false;
            webView.loadUrl("loadUrl('about:blank')");
            webView.clearCache(true);
            webView.loadUrl("file:///android_asset/Web.html");

        });
    }

}
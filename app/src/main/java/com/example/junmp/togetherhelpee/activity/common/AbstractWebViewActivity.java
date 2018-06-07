package com.example.junmp.togetherhelpee.activity.common;

import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.junmp.togetherhelpee.R;

public abstract class AbstractWebViewActivity extends AppCompatActivity {
    protected WebView webView;

    protected void initWebview(int viewName) {
        webView = findViewById(viewName);
        webView.setWebViewClient(new WebViewClient()); // 이걸 안해주면 새창이 뜸
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    protected void showWebView(String url) {
        webView.loadUrl(url);
    }

}

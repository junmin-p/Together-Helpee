package com.example.junmp.togetherhelpee.activity.volunteer.request.form;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.webkit.JavascriptInterface;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.common.AbstractWebViewActivity;
import com.example.junmp.togetherhelpee.activity.home.HomeActivity;
import com.example.junmp.togetherhelpee.common.constante.Server;

public class RegisterDoneActivity extends AbstractWebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_request_done);
        super.initWebview(R.id.webview);
        int volunteerId = getIntent().getIntExtra("volunteerId" , 0);
        showWebView(Server.WEB_VIEW_ROOT + "/user/volunteer/" +volunteerId+ "/register/done");
        bindJavascript();
    }

    private void bindJavascript() {
        webView.addJavascriptInterface(new Object() {

            @JavascriptInterface
            public void clickHome() {
                startActivity(new Intent(RegisterDoneActivity.this , HomeActivity.class));
                finish();
            }
        } , "Volunteer");
    }
}

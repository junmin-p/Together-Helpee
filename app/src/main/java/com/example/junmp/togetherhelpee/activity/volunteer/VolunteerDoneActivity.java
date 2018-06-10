package com.example.junmp.togetherhelpee.activity.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.common.AbstractWebViewActivity;
import com.example.junmp.togetherhelpee.activity.home.HomeActivity;
import com.example.junmp.togetherhelpee.common.constante.Server;

public class VolunteerDoneActivity extends AbstractWebViewActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_request_done);
        super.initWebview(R.id.webview);
        int volunteerId = getIntent().getIntExtra("volunteerId" , 0);
        showWebView(Server.WEB_VIEW_ROOT + "/volunteer/" +volunteerId+ "/done");
        bindJavascript();
    }

    private void bindJavascript() {
        webView.addJavascriptInterface(new Object() {

            @JavascriptInterface
            public void clickHome() {
                startActivity(new Intent(VolunteerDoneActivity.this , HomeActivity.class));
                finish();
            }
        } , "Volunteer");
    }
}

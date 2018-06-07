package com.example.junmp.togetherhelpee.activity.user.register.form;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.common.AbstractWebViewActivity;
import com.example.junmp.togetherhelpee.common.constante.Server;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUUIDFactory;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUtil;
import com.example.junmp.togetherhelpee.domain.user.UserService;


public class AgeFormActivity extends AbstractWebViewActivity {

    private UserService userService = new UserService();
    private String imageName;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form_age);
        initWebview(R.id.webview);

        imageName = getIntent().getStringExtra("imageName");
        name = getIntent().getStringExtra("name");
        showWebView(Server.WEB_VIEW_ROOT + "/user/register/age");
        bindJavascript();
    }

    private void bindJavascript() {
        webView.addJavascriptInterface(new Object() {

            @JavascriptInterface
            public void next(String age) {
                Intent intent = new Intent(AgeFormActivity.this, GenderFormActivity.class);
                intent.putExtra("imageName" , imageName);
                intent.putExtra("name" , name);
                intent.putExtra("age" , age);
                startActivity(intent);
                finish();
            }

        }, "User");
    }
}
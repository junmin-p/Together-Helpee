package com.example.junmp.togetherhelpee.activity.user.register.form;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.common.AbstractWebViewActivity;
import com.example.junmp.togetherhelpee.common.constante.Server;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUUIDFactory;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUtil;
import com.example.junmp.togetherhelpee.domain.user.UserService;


public class GenderFormActivity extends AbstractWebViewActivity {

    private UserService userService = new UserService();
    private String imageName;
    private String name;
    private int age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form_gender);
        initWebview(R.id.webview);

        age = getIntent().getIntExtra("age" , 0);
        imageName = getIntent().getStringExtra("imageName");
        name = getIntent().getStringExtra("name");

        showWebView(Server.WEB_VIEW_ROOT + "/user/register/gender");
        bindJavascript();
    }

    private void bindJavascript() {
        webView.addJavascriptInterface(new Object() {

            @JavascriptInterface
            public void register(String gender) {
                new AsyncInit().execute(gender);
            }

        }, "User");
    }

    private class AsyncInit extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String gender = params[0];

            userService.register(age, name, imageName, gender ,  new DeviceUUIDFactory(getApplicationContext()).getDeviceUuid(), DeviceUtil.getPhoneNumber(GenderFormActivity.this));

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            startActivity(new Intent(GenderFormActivity.this, RegisterDoneActivity.class));
            finish();
        }
    }
}
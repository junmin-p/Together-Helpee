package com.example.junmp.togetherhelpee.activity.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.common.AbstractWebViewActivity;
import com.example.junmp.togetherhelpee.activity.home.HomeActivity;
import com.example.junmp.togetherhelpee.common.constante.Server;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUtil;
import com.example.junmp.togetherhelpee.domain.user.User;
import com.example.junmp.togetherhelpee.domain.user.UserService;

public class UserActivity extends AbstractWebViewActivity {
    private UserService userService = new UserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initWebview(R.id.webview);
        new AsyncInit().execute();
    }

    private void bindJavascript() {
        webView.addJavascriptInterface(new Object() {

            @JavascriptInterface
            public void home() {
                Intent intent = new Intent(UserActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

        }, "User");
    }

    private class AsyncInit extends AsyncTask<String, Void, User> {


        @Override
        protected User doInBackground(String... strings) {
            return userService.getLoggedUser(DeviceUtil.getPhoneNumber(UserActivity.this));
        }

        @Override
        protected void onPostExecute(User user) {
            showWebView(Server.WEB_VIEW_ROOT + "/user/" + user.getId());
            bindJavascript();
        }
    }
}

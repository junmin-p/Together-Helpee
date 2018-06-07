package com.example.junmp.togetherhelpee.activity.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.*;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.common.AbstractWebViewActivity;
import com.example.junmp.togetherhelpee.activity.volunteer.request.form.RegisterWebViewFormActivity;
import com.example.junmp.togetherhelpee.common.constante.Server;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUtil;
import com.example.junmp.togetherhelpee.domain.user.User;
import com.example.junmp.togetherhelpee.domain.user.UserService;

public class HomeWebViewActivity extends AbstractWebViewActivity {

    private UserService userService = new UserService();

    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_webview);
        super.initWebview(R.id.home_index);
        new AsyncInit().execute();
        webView.addJavascriptInterface(new Object() {

            @JavascriptInterface
            public void help() {
                startActivity(new Intent(HomeWebViewActivity.this , RegisterWebViewFormActivity.class));
            }

            @JavascriptInterface
            public void register() {

            }
        }, "Home");
    }



    private class AsyncInit extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            user = userService.getLoggedUser(DeviceUtil.getPhoneNumber(HomeWebViewActivity.this));
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user == null)
                showWebView(Server.WEB_VIEW_ROOT);
            else
                showWebView(Server.WEB_VIEW_ROOT + "/" + user.getId());
        }
    }
}

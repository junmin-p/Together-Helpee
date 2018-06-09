package com.example.junmp.togetherhelpee.activity.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.webkit.*;
import com.example.junmp.togetherhelpee.FeedbackActivity;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.common.AbstractWebViewActivity;
import com.example.junmp.togetherhelpee.activity.user.UserActivity;
import com.example.junmp.togetherhelpee.activity.user.register.form.FaceFormActivity;
import com.example.junmp.togetherhelpee.activity.volunteer.request.form.RegisterFormActivity;
import com.example.junmp.togetherhelpee.common.constante.Server;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUtil;
import com.example.junmp.togetherhelpee.domain.user.User;
import com.example.junmp.togetherhelpee.domain.user.UserService;

public class HomeActivity extends AbstractWebViewActivity {

    private UserService userService = new UserService();

    private User user;
    private int volunteerId = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_webview);
        super.initWebview(R.id.home_index);
        final SwipeRefreshLayout refreshLayout = findViewById(R.id.swipe_refresh);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                refreshLayout.setRefreshing(false);
            }
        });

        Intent intent = getIntent();
        volunteerId = intent.getIntExtra("volunteerId" , 0);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_orange_light
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncInit().execute();
    }

    private void bindJavascript() {
        webView.addJavascriptInterface(new Object() {

            @JavascriptInterface
            public void help() {
                startActivity(new Intent(HomeActivity.this, RegisterFormActivity.class));

            }

            @JavascriptInterface
            public void feedback() {
                startActivity(new Intent(HomeActivity.this, FeedbackActivity.class));
            }

            @JavascriptInterface
            public void register() {
                startActivity(new Intent(HomeActivity.this, FaceFormActivity.class));

            }

            @JavascriptInterface
            public void userHome() {
                startActivity(new Intent(HomeActivity.this, UserActivity.class));
            }
        }, "Home");
    }

    private class AsyncInit extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            user = userService.getLoggedUser(DeviceUtil.getPhoneNumber(HomeActivity.this));
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user == null)
                showWebView(Server.WEB_VIEW_ROOT);
            else
                showWebView(Server.WEB_VIEW_ROOT + "/" + user.getId() + "?volunteerId="+ volunteerId);

            bindJavascript();
        }
    }
}

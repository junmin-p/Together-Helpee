package com.example.junmp.togetherhelpee.activity.user.register.form;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.common.AbstractWebViewActivity;
import com.example.junmp.togetherhelpee.activity.home.HomeActivity;
import com.example.junmp.togetherhelpee.common.constante.Server;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUUIDFactory;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUtil;
import com.example.junmp.togetherhelpee.domain.user.User;
import com.example.junmp.togetherhelpee.domain.user.UserService;

public class RegisterDoneActivity extends AbstractWebViewActivity {

    private UserService userService = new UserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_done);
        initWebview(R.id.webview);


        new AsyncInit().execute(DeviceUtil.getPhoneNumber(this));
    }


    private void bindJavascript() {
        webView.addJavascriptInterface(new Object() {

            @JavascriptInterface
            public void next(int age) {
                Intent intent = new Intent(RegisterDoneActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

        }, "User");
    }

    private class AsyncInit extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {


            User loggedUser = userService.getLoggedUser(params[0]);

            return loggedUser;
        }

        @Override
        protected void onPostExecute(User user) {
            showWebView(Server.WEB_VIEW_ROOT + "/user/"+user.getId()+"/register/done");
            bindJavascript();
        }
    }
}

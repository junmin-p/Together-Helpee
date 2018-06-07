package com.example.junmp.togetherhelpee.activity.user.register.form;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.common.AbstractWebViewActivity;
import com.example.junmp.togetherhelpee.common.constante.Server;

public class FaceFormActivity extends AbstractWebViewActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_face_form);
        super.initWebview(R.id.webview);
        showWebView(Server.WEB_VIEW_ROOT + "/user/register/face");
        bindJavascript();
    }

    private void bindJavascript() {
        webView.addJavascriptInterface(new Object() {

            @JavascriptInterface
            public void clickCamera() {
                startActivity(new Intent(FaceFormActivity.this, CameraActivity.class));
                finish();
            }

        }, "User");
    }
}

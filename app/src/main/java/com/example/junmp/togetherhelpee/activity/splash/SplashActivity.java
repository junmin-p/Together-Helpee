package com.example.junmp.togetherhelpee.activity.splash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.home.HomeActivity;
import com.example.junmp.togetherhelpee.common.util.DeviceUUIDFactory;
import com.example.junmp.togetherhelpee.common.util.push.PushUtil;
import com.example.junmp.togetherhelpee.domain.device.DeviceService;

public class SplashActivity extends AppCompatActivity {
    private DeviceService deviceService = new DeviceService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new NetworkCall().execute();
    }

    private class NetworkCall extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            deviceService.save(new DeviceUUIDFactory(getApplicationContext()).getDeviceUuid() , PushUtil.getToken());
            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(SplashActivity.this , HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

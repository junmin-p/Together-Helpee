package com.example.junmp.togetherhelpee.activity.splash;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.home.HomeActivity;
import com.example.junmp.togetherhelpee.activity.home.HomeWebViewActivity;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUUIDFactory;
import com.example.junmp.togetherhelpee.common.util.push.PushUtil;
import com.example.junmp.togetherhelpee.domain.device.DeviceService;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private DeviceService deviceService = new DeviceService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.CAMERA , Manifest.permission.RECORD_AUDIO , Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.READ_PHONE_NUMBERS , Manifest.permission.READ_PHONE_STATE)
                .check();
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            new NetworkCall().execute();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(SplashActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            new NetworkCall().execute();
        }
    };

    private class NetworkCall extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            deviceService.save(new DeviceUUIDFactory(getApplicationContext()).getDeviceUuid(), PushUtil.getToken());
            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(SplashActivity.this, HomeWebViewActivity.class);
            startActivity(intent);
            finish();
        }
    }


}

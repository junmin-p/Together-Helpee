package com.example.junmp.togetherhelpee;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

public class processTimerReceiver extends BroadcastReceiver {
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private GpsInfo gps;

    double longitude;
    double latitude;
    int volunteerId;

    postLocation postLocation;
    @Override
    public void onReceive(Context context, Intent intent) {
        gps = new GpsInfo(context);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            volunteerId = intent.getIntExtra("volunteerId", 0);

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Log.d("fdasds",String.valueOf(latitude));
            Log.d("fdasds",String.valueOf(longitude));

            postLocation = new postLocation();
            postLocation.execute("http://210.89.191.125/helpee/location");
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }

    }

    class postLocation extends AsyncTask<String, Void, String> {
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("fasd","success");
        }

        @Override
        protected String doInBackground(String... params) {
            String UPLOAD_URL = params[0];
            HashMap<String, String> data = new HashMap<>();
            data.put("helpeeLongitude", String.valueOf(longitude));
            data.put("helpeeLatitude", String.valueOf(latitude));
            data.put("volunteerId", String.valueOf(volunteerId));



            String result = rh.sendPostRequest(UPLOAD_URL, data);

            return result;
        }
    }

}

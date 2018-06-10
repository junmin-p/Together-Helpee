package com.example.junmp.togetherhelpee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;

public class processTimerReceiver2 extends BroadcastReceiver {
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private GpsInfo gps;

    double longitude;
    double latitude;
    int volunteerId;
    String phone_num;

    putLoc putLoc;
    @Override
    public void onReceive(Context context, Intent intent) {
        gps = new GpsInfo(context);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            phone_num = intent.getStringExtra("phonenum");

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Log.d("fdasds",String.valueOf(latitude));
            Log.d("fdasds",String.valueOf(longitude));

            putLoc = new putLoc();
            putLoc.execute("http://210.89.191.125/helpee/real-matching/location");
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }

    }

    class putLoc extends AsyncTask<String, Void, String> {
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String UPLOAD_URL = params[0];
            HashMap<String, String> data = new HashMap<>();
            data.put("userId", phone_num);
            data.put("latitude", String.valueOf(latitude));
            data.put("longitude", String.valueOf(longitude));

            String result = rh.sendPutRequest(UPLOAD_URL, data);

            return result;
        }
    }

}

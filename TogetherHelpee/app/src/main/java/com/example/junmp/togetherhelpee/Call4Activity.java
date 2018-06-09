package com.example.junmp.togetherhelpee;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Call4Activity extends AppCompatActivity {
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    private GpsInfo gps;

    double latitude;
    double longitude;

    private RadioGroup radioGroup;
    private RadioButton one, two, three, four, five;
    private int checked = 1;

    private RadioGroup radioGroup2;
    private RadioButton outside, talk, housework, education;
    private String type = "outside";

    private String phone_num;
    private String etc;
    private String dest_date;
    private String dest_time;

    postPush postPush;
    getReserve getReserve;
    getReserve2 getReserve2;

    String mJsonString;

    String helperId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call4);

        Intent from1 = getIntent();
        if(from1.getStringExtra("phonenum") != null){
            phone_num = from1.getStringExtra("phonenum");
        }
        if(from1.getStringExtra("etc") != null){
            etc = from1.getStringExtra("etc");
        }
        if(from1.getStringExtra("date") != null){
            dest_date = from1.getStringExtra("date");
        }
        if(from1.getStringExtra("time") != null){
            dest_time = from1.getStringExtra("time");
        }

        radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.one) {
                    checked = 1;
                } else if(checkedId == R.id.two) {
                    checked = 2;
                } else if(checkedId == R.id.three) {
                    checked = 3;
                } else if(checkedId == R.id.four) {
                    checked = 4;
                } else {
                    checked = 5;
                }
            }

        });

        one = (RadioButton) findViewById(R.id.one);
        two = (RadioButton) findViewById(R.id.two);
        three = (RadioButton) findViewById(R.id.three);
        four = (RadioButton) findViewById(R.id.four);
        five = (RadioButton) findViewById(R.id.five);

        radioGroup2 = (RadioGroup) findViewById(R.id.myRadioGroup2);

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.outside) {
                    type = "outside";
                } else if(checkedId == R.id.talk) {
                    type = "talk";
                } else if(checkedId == R.id.housework) {
                    type = "housework";
                } else {
                    type = "education";
                }
            }

        });

        one = (RadioButton) findViewById(R.id.one);
        two = (RadioButton) findViewById(R.id.two);
        three = (RadioButton) findViewById(R.id.three);
        four = (RadioButton) findViewById(R.id.four);
        five = (RadioButton) findViewById(R.id.five);

        Button btn_back = findViewById(R.id.btn_no);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(Call4Activity.this, Call3Activity.class);
                back.putExtra("phonenum",phone_num);
                back.putExtra("date",dest_date);
                back.putExtra("time",dest_time);
                startActivity(back);
                finish();
            }
        });

        Button btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (!isPermission) {
                        callPermission();
                        return;
                    }

                    gps = new GpsInfo(Call4Activity.this);
                    // GPS 사용유무 가져오기
                    if (gps.isGetLocation()) {
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        Log.d("fdasds",String.valueOf(latitude));
                        Log.d("fdasds",String.valueOf(longitude));

                        postRequest pR = new postRequest();
                        pR.execute("http://210.89.191.125/helpee/volunteer");

                        Toast.makeText(
                                getApplicationContext(),
                                "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                                Toast.LENGTH_LONG).show();
                    } else {
                        // GPS 를 사용할수 없으므로
                        gps.showSettingsAlert();
                    }
                }



        });
        callPermission();
    }

    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }
    class postRequest extends AsyncTask<String, Void, String> {
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            getReserve = new getReserve();
            getReserve.execute("http://210.89.191.125/helpee/reservation");
        }

        @Override
        protected String doInBackground(String... params) {
            String UPLOAD_URL = params[0];
            HashMap<String, String> data = new HashMap<>();
            data.put("type", type);
            data.put("userPhone", phone_num);
            data.put("longitude", String.valueOf(longitude));
            data.put("latitude", String.valueOf(latitude));
            data.put("content", etc);
            data.put("date", dest_date);
            data.put("time", dest_time);
            data.put("duration", String.valueOf(checked));



            String result = rh.sendPostRequest(UPLOAD_URL, data);

            return result;
        }
    }

    private class postPush extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Intent toCall = new Intent(Call4Activity.this, Call1Activity.class);
            toCall.putExtra("phonenum", phone_num);
            startActivity(toCall);
            finish();
        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0]+"?latitude="+latitude+"&&longitude="+longitude;

            Log.d("Asd",serverURL);
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("fadsfsads", "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d("fadsfsads", "InsertData: Error ", e);
                errorString = e.toString();


                return null;
            }

        }
    }

    private class getReserve extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("Asd",result);
            if (result.equals("")){
                postPush = new postPush();
                postPush.execute("http://210.89.191.125/helpee/helpers/push");
            }
            else {
                mJsonString = result;
                try {
                    showResult();
                } catch (JSONException e) {
                    Log.d("fda","asd");
                    e.printStackTrace();
                }
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0]+"?latitude="+latitude+"&&longitude="+longitude+"&&date="+dest_date+"%"+dest_time;

            Log.d("Asd",serverURL);
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("fadsfsads", "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d("fadsfsads", "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult() throws JSONException {
        try {
            JSONArray jsonArray = new JSONArray(mJsonString);

            for(int i=jsonArray.length()-1;i>=0;i--){
                JSONObject item = jsonArray.getJSONObject(i);

                helperId = item.getString("userId");

                getReserve = new getReserve();
                getReserve.execute("http://210.89.191.125/helpee/reservation");
            }

        } catch (JSONException e) {
            Log.d("fadsfsads", "showResult : ", e);
        }
    }

    private class getReserve2 extends AsyncTask<String, Void, String> {
        String errorString2 = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("Asd",result);
            if (result.equals("")){
                Intent toError = new Intent(Call4Activity.this, ErrorActivity.class);
                startActivity(toError);
                finish();
            }
            else {
                int volunteerId = Integer.parseInt(result);
                Intent toPartner = new Intent(Call4Activity.this, PartnerActivity.class);
                toPartner.putExtra("where", "reserve");
                toPartner.putExtra("latitude", latitude);
                toPartner.putExtra("longitude", longitude);
                toPartner.putExtra("helperId", helperId);
                toPartner.putExtra("phonenum",phone_num);
                toPartner.putExtra("type", type);
                toPartner.putExtra("content", etc);
                toPartner.putExtra("time", dest_time);
                toPartner.putExtra("duration", checked);
                toPartner.putExtra("date", dest_date);
                toPartner.putExtra("volunteerId", volunteerId);
                startActivity(toPartner);
                finish();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0]+"?latitude="+latitude+"&&longitude="+longitude+"&&date="+dest_date+"%"+dest_time;

            Log.d("Asd",serverURL);
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("fadsfsads", "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d("fadsfsads", "InsertData: Error ", e);
                errorString2 = e.toString();

                return null;
            }

        }
    }


}

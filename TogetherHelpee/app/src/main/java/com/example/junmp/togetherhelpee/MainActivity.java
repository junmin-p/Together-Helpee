package com.example.junmp.togetherhelpee;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button btn_sign;
    String check;
    String PhoneNum;

    Check checkdevice;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sign = findViewById(R.id.btn_sign);

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toFace = new Intent(MainActivity.this, FaceActivity.class);
                toFace.putExtra("from", "first");
                toFace.putExtra("phonenum", PhoneNum);

                startActivity(toFace);
                finish();
            }
        });

        TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try{
            PhoneNum = mgr.getLine1Number();//mgr.getLine1Number();
            PhoneNum = PhoneNum.replace("+82", "0");
            Toast.makeText(getApplicationContext(), PhoneNum, Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        PhoneNum = "6";

        checkdevice = new Check();
        checkdevice.execute("http://210.89.191.125/helpee/user");

    }

    private class Check extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result == null){
                Intent toError = new Intent(MainActivity.this, ErrorActivity.class);
                startActivity(toError);
                finish();
            }

            else if (result.equals("true")) {
                Intent toCall = new Intent(MainActivity.this, CallActivity.class);
                toCall.putExtra("phonenum", PhoneNum);
                startActivity(toCall);
                finish();
            } else {
                Toast.makeText(MainActivity.this,"회원가입을 먼저 해주세요.",Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0] + "/" + PhoneNum;
            Log.d("ASD",serverURL);
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {
                errorString = e.toString();

                return null;
            }

        }
    }
}

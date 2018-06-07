package com.example.junmp.togetherhelpee;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.junmp.togetherhelpee.activity.user.register.form.CameraActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button btn_sign;
    String check;
    String PhoneNum;

    String idByTelephonyManager;

    checkUser checkUser;
    postKey postKey;
    putKey putKey;
    checkDevice checkDevice;

    private static final int REQUEST_READ_PHONE_STATE_PERMISSION = 225;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView Togethericon = (ImageView) findViewById(R.id.img_logo);

        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(Togethericon);
        Glide.with(this).load(R.drawable.logo).into(gifImage);

        btn_sign = findViewById(R.id.btn_sign);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            idByTelephonyManager = mgr.getDeviceId();

            try {
                PhoneNum = mgr.getLine1Number();//mgr.getLine1Number();
                PhoneNum = PhoneNum.replace("+82", "0");
            } catch (Exception e) {
            }
        } else {
            Log.d("phony", "Current app does not have READ_PHONE_STATE permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_PHONE_STATE_PERMISSION);
        }


        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toFace = new Intent(MainActivity.this, CameraActivity.class);
                toFace.putExtra("from", "first");
                toFace.putExtra("phonenum", PhoneNum);
                toFace.putExtra("deviceKey", idByTelephonyManager);

                startActivity(toFace);
                finish();
            }
        });


        checkDevice = new checkDevice();
        checkDevice.execute("http://210.89.191.125/helpee/device");


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("phony", "Permission Granted");
                    //Proceed to next steps

                } else {
                    Log.e("phony", "Permission Denied");
                }
                return;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private class checkDevice extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {
                Intent toError = new Intent(MainActivity.this, ErrorActivity.class);
                startActivity(toError);
                finish();
            } else if (result.equals("true")) {
                putKey = new putKey();
                putKey.execute("http://210.89.191.125/helpee/token/update");
            } else {
                postKey = new postKey();
                postKey.execute("http://210.89.191.125/helpee/device/save");
            }

        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0] + "/" + idByTelephonyManager;
            Log.d("ASD", serverURL);
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

    class postKey extends AsyncTask<String, Void, String> {
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            checkUser = new checkUser();
            checkUser.execute("http://210.89.191.125/helpee/user");
        }

        @Override
        protected String doInBackground(String... params) {
            String UPLOAD_URL = params[0];
            HashMap<String, String> data = new HashMap<>();
            String token = FirebaseInstanceId.getInstance().getToken();
            data.put("token", token);
            data.put("deviceKey", idByTelephonyManager);

            String result = rh.sendPostRequest(UPLOAD_URL, data);

            return result;
        }
    }

    class putKey extends AsyncTask<String, Void, String> {
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            checkUser = new checkUser();
            checkUser.execute("http://210.89.191.125/helpee/user");
        }

        @Override
        protected String doInBackground(String... params) {
            String UPLOAD_URL = params[0];
            HashMap<String, String> data = new HashMap<>();
            String token = FirebaseInstanceId.getInstance().getToken();
            data.put("token", token);
            data.put("deviceKey", idByTelephonyManager);

            String result = rh.sendPutRequest(UPLOAD_URL, data);

            return result;
        }
    }

    private class checkUser extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {
                Intent toError = new Intent(MainActivity.this, ErrorActivity.class);
                startActivity(toError);
                finish();
            } else if (result.equals("true")) {
                Intent toCall = new Intent(MainActivity.this, CallActivity.class);
                toCall.putExtra("phonenum", PhoneNum);
                startActivity(toCall);
                finish();
            } else {
                Log.d("ASD", result);
                btn_sign.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "화면 중앙의 회원가입버튼을 클릭하면 자동으로 카메라가 켜지며 얼굴인식 시 자동 촬영됩니다. 회원가입은 사진촬영만으로 완료됩니다.", Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0] + "/" + PhoneNum;
            Log.d("ASD", serverURL);
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

package com.example.junmp.togetherhelpee;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.junmp.togetherhelpee.activity.home.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class IngActivity extends AppCompatActivity {
    String phone_num;
    int volunteerId;

    String mJsonString;

    getVolunteer getVolunteer;

    private TimerTask mTask2;
    private Timer mTimer2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ing);

        Toast.makeText(getApplicationContext(),"현재 도움을 받으시고 있으시군요? 봉사자가 종료하면 자동으로 다음화면으로 넘어갑니다.",Toast.LENGTH_LONG).show();


        Intent getId = getIntent();
        volunteerId = getId.getIntExtra("volunteerId" , 0);
        if(getId.getStringExtra("phonenum") != null){
            phone_num = getId.getStringExtra("phonenum");
        }


        int repeatTime = 10;  //Repeat alarm time in seconds
        AlarmManager processTimer = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, processTimerReceiver.class);
        intent.putExtra("volunteerId",volunteerId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,  intent, PendingIntent.FLAG_UPDATE_CURRENT);
//Repeat alarm every second
        processTimer.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),repeatTime*1000, pendingIntent);

        getVolunteer = new getVolunteer();
        getVolunteer.execute("http://210.89.191.125/helpee/volunteers/wait/");
        Log.d("asdfads", String.valueOf(volunteerId));


    }

    private class getVolunteer extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("Asd",result);
            if (result.equals("[]")){
                Intent toFeed = new Intent(IngActivity.this, FeedbackActivity.class);
                toFeed.putExtra("volunteerId",volunteerId);
                startActivity(toFeed);
                finish();
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
            String serverURL = params[0]+phone_num;

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

                int startStatus = item.getInt("startStatus");

                if(startStatus == 2){
                    Toast.makeText(getApplicationContext(),"봉사가 종료되었습니다.",Toast.LENGTH_SHORT).show();

                    Intent toMain = new Intent(IngActivity.this, MainActivity.class);
                    startActivity(toMain);
                    finish();
                }
            }

        } catch (JSONException e) {
            Log.d("fadsfsads", "showResult : ", e);
        }
    }
}

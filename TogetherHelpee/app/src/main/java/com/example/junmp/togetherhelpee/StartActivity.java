package com.example.junmp.togetherhelpee;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class StartActivity extends AppCompatActivity {
    int volunteerId;
    String phone_num;

    String helperId;
    String helper_name;

    TextView txt_time;
    TextView txt_helper;
    Button btn_start;

    getName getName;
    putStart putStart;

    String mJsonString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toast.makeText(getApplicationContext(),"하단의 봉사시작 버튼을 클릭하시면 봉사활동이 시작됩니다!",Toast.LENGTH_LONG).show();

        Intent fromCall = getIntent();
        String type = fromCall.getStringExtra("type");
        if(type.equals("outside")){
            type = "외출";
        }
        else if(type.equals("talk")){
            type = "말동무";
        }
        else if(type.equals("housework")){
            type = "가사";
        }
        else if(type.equals("education")){
            type = "교육";
        }
        helperId = fromCall.getStringExtra("helperId");
        String content = fromCall.getStringExtra("content");
        String time = fromCall.getStringExtra("time");
        int duration = fromCall.getIntExtra("duration", 0);
        String date = fromCall.getStringExtra("date");
        volunteerId = fromCall.getIntExtra("volunteerId", 0);

        phone_num = fromCall.getStringExtra("phonenum");

        txt_time = findViewById(R.id.txt_time);
        txt_helper = findViewById(R.id.txt_helper);
        btn_start = findViewById(R.id.btn_start);

        txt_time.setText("약속시간은 \n날짜:"+date+"\n"+"시간:"+time+"입니다.");

        long current_time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = dayTime.format(new Date(current_time));

        String dest_time = date+" "+time;

        Date current = null;
        Date dest = null;
        try {
            current = dayTime.parse(str);
            dest = dayTime.parse(dest_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = dest.getTime() - current.getTime();
        long diffSeconds = diff / 1000;

        if(diffSeconds >= 0){
            btn_start.setText("아직 시작시간이 되지 않았습니다.");
            btn_start.setEnabled(false);
        }

        getName = new getName();
        getName.execute("http://210.89.191.125/helpee/helper/name/");

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_start.isEnabled()){
                    putStart = new putStart();
                    putStart.execute("http://210.89.191.125/helpee/volunteer/start");
                }
                else{
                    btn_start.setText("아직 시작시간이 되지 않았습니다.");
                }
            }
        });
    }

    private class getName extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("[]")){
                Intent toError = new Intent(StartActivity.this, ErrorActivity.class);
                startActivity(toError);
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
            String serverURL = params[0]+helperId;
            Log.d("Asd", serverURL);
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

                helper_name = item.getString("name");
            }

        } catch (JSONException e) {
            Log.d("fadsfsads", "showResult : ", e);
        }
    }

    class putStart extends AsyncTask<String, Void, String> {
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"봉사를 시작합니다.", Toast.LENGTH_SHORT).show();

            Intent toIng = new Intent(StartActivity.this, IngActivity.class);
            toIng.putExtra("volunteerId", volunteerId);
            toIng.putExtra("phonenum", phone_num);
            startActivity(toIng);
            finish();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<>();
            data.put("volunteerId", String.valueOf(volunteerId));
            String result = rh.sendPutRequest(params[0], data);

            return result;
        }
    }
}

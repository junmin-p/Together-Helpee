package com.example.junmp.togetherhelpee;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MatchActivity extends AppCompatActivity {
    String phone_num;

    TextView txt_type;
    TextView txt_date;
    TextView txt_time;
    TextView txt_duration;
    TextView txt_content;
    TextView txt_match_state;
    TextView txt_match_info;

    Button btn_accept;
    Button btn_cancel;

    String type;
    String helperId;
    String content;
    String time;
    int duration;
    String date;
    int volunteerId;

    int matchingStatus;

    String mJsonString;

    deleteRequest deleteRequest;
    putRequest putRequest;
    getState getState;

    private TimerTask mTask1;
    private Timer mTimer1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Toast.makeText(getApplicationContext(),"우측하단의 버튼을 클릭하면 도움요청이 취소되고, 도움을 주겠다는 봉사자가 자원하면 좌측하단의 수락버튼이 나타납니다.",Toast.LENGTH_LONG).show();
        Intent fromCall = getIntent();
        type = fromCall.getStringExtra("type");
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
        matchingStatus = fromCall.getIntExtra("matchingStatus", 0);
        content = fromCall.getStringExtra("content");
        time = fromCall.getStringExtra("time");
        duration = fromCall.getIntExtra("duration", 0);
        date = fromCall.getStringExtra("date");
        volunteerId = fromCall.getIntExtra("volunteerId" , 0);

        phone_num = fromCall.getStringExtra("phonenum");

        txt_type = findViewById(R.id.txt_type);
        txt_date = findViewById(R.id.txt_date);
        txt_time = findViewById(R.id.txt_time);
        txt_duration = findViewById(R.id.txt_duration);
        txt_content = findViewById(R.id.txt_content);
        txt_match_state = findViewById(R.id.txt_match_state);
        txt_match_info = findViewById(R.id.txt_match_info);
        btn_accept = findViewById(R.id.btn_accept);
        btn_cancel = findViewById(R.id.btn_cancel);

        getState  = new getState();
        getState.execute("http://210.89.191.125/helpee/volunteers/wait/");

        txt_type.setText("요청 종류: "+type);
        txt_date.setText("요청 날짜: "+date);
        txt_time.setText("요청 시각: "+time);
        txt_duration.setText("요청 봉사시간: "+duration);
        txt_content.setText("요청 기타사항: "+content);

        mTask1 = new TimerTask() {
            @Override
            public void run() {
                getState  = new getState();
                getState.execute("http://210.89.191.125/helpee/volunteers/wait/");
            }
        };

        mTimer1 = new Timer();

        mTimer1.schedule(mTask1, 3000, 2000);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putRequest = new putRequest();
                putRequest.execute("http://210.89.191.125/helpee/volunteer/complete");
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRequest = new deleteRequest();
                deleteRequest.execute("http://210.89.191.125/helpee/volunteer/delete");
            }
        });

    }
    private class getState extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("[]")){
                Intent toError = new Intent(MatchActivity.this, ErrorActivity.class);
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

                if(matchingStatus == 1){
                    mTimer1.cancel();
                    txt_match_state.setText("매칭 수락 대기중입니다!");
                    txt_match_info.setText("지원한 봉사자의 아이디: "+helperId);
                    btn_accept.setVisibility(View.VISIBLE);
                }
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0]+phone_num;

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

            int i=jsonArray.length()-1;
            JSONObject item = jsonArray.getJSONObject(i);

            helperId = item.getString("helperId");
            matchingStatus = item.getInt("matchingStatus");

            Log.d("dafdssafs",helperId);
            Log.d("dafdssafs",matchingStatus+"");


        } catch (JSONException e) {
            Log.d("fadsfsads", "showResult : ", e);
        }
    }

    class putRequest extends AsyncTask<String, Void, String> {
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"요청을 수락합니다.", Toast.LENGTH_SHORT).show();
            Intent toStart = new Intent(MatchActivity.this, StartActivity.class);
            toStart.putExtra("type", type);
            toStart.putExtra("helperId", helperId);
            toStart.putExtra("content", content);
            toStart.putExtra("time", time);
            toStart.putExtra("duration", duration);
            toStart.putExtra("date", date);
            toStart.putExtra("volunteerId", volunteerId);
            toStart.putExtra("phonenum", phone_num);
            startActivity(toStart);
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

    class deleteRequest extends AsyncTask<String, Void, String> {
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"요청이 취소됩니다.", Toast.LENGTH_SHORT).show();
            Intent toCall = new Intent(MatchActivity.this, CallActivity.class);
            toCall.putExtra("phonenum", phone_num);
            startActivity(toCall);
            finish();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<>();
            data.put("volunteerId", String.valueOf(volunteerId));
            String result = rh.sendDeleteRequest(params[0], data);

            return result;
        }
    }
}
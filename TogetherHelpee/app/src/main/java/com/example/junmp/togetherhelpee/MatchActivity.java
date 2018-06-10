package com.example.junmp.togetherhelpee;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MatchActivity extends AppCompatActivity {
    String phone_num;

    TextView txt_date;
    TextView txt_content;

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
    getState getState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        txt_date = findViewById(R.id.txt_date);
        txt_content = findViewById(R.id.txt_content);
        btn_cancel = findViewById(R.id.btn_cancel);

        getState  = new getState();
        getState.execute("http://210.89.191.125/helpee/volunteers/wait/");

        txt_date.setText(" "+date+" "+time);
        txt_content.setText(" "+"\""+content+"\"");




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
                    Intent toPartner = new Intent(MatchActivity.this, PartnerActivity.class);
                    toPartner.putExtra("type", type);
                    toPartner.putExtra("helperId", helperId);
                    toPartner.putExtra("content", content);
                    toPartner.putExtra("time", time);
                    toPartner.putExtra("duration", duration);
                    toPartner.putExtra("date", date);
                    toPartner.putExtra("volunteerId", volunteerId);
                    toPartner.putExtra("phonenum", phone_num);

                    startActivity(toPartner);
                    finish();
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
            Intent toCall = new Intent(MatchActivity.this, Call1Activity.class);
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
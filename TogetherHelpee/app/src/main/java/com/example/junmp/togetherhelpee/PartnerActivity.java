package com.example.junmp.togetherhelpee;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class PartnerActivity extends AppCompatActivity {
    Button btn_yes;
    Button btn_no;

    putRequest putRequest;
    putReject putReject;

    String type;
    String helperId;
    String content;
    String time;
    int duration;
    String date;
    int volunteerId;
    String phone_num;

    getName getName;
    String mJsonString;

    TextView txt_name;
    TextView txt_career;
    TextView txt_score;
    ImageView img_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

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
        content = fromCall.getStringExtra("content");
        time = fromCall.getStringExtra("time");
        duration = fromCall.getIntExtra("duration", 0);
        date = fromCall.getStringExtra("date");
        volunteerId = fromCall.getIntExtra("volunteerId", 0);

        phone_num = fromCall.getStringExtra("phonenum");

        getName = new getName();
        getName.execute("http://210.89.191.125/helpee/helper/name/");

        txt_name = findViewById(R.id.txt_name);
        txt_career = findViewById(R.id.txt_career);
        txt_score = findViewById(R.id.txt_score);
        img_profile = findViewById(R.id.img_profile);

        btn_yes = findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putRequest = new putRequest();
                putRequest.execute("http://210.89.191.125/helpee/volunteer/complete");
            }
        });

        btn_no = findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putReject = new putReject();
                putRequest.execute("http://210.89.191.125/helpee/volunteer/reject");
            }
        });
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
            Intent toStart = new Intent(PartnerActivity.this, StartActivity.class);
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

    class putReject extends AsyncTask<String, Void, String> {
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Intent toCall = new Intent(PartnerActivity.this, Call1Activity.class);
            startActivity(toCall);
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
                Intent toError = new Intent(PartnerActivity.this, ErrorActivity.class);
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

                String helper_name = item.getString("name");
                int admit_time = item.getInt("admitTime");
                double helper_score = item.getDouble("userFeedbackScore");
                String img_src = "http://210.89.191.125/photo/"+item.getString("profileImage");

                txt_name.setText("이름 : "+helper_name);
                txt_career.setText("봉사경력 : "+admit_time+"시간");
                Log.d("FDAsfs",helper_score+"");
                txt_score.setText("평점 : "+String.format("%.2f",helper_score)+"점");
                new DownloadImageTask(img_profile).execute(img_src);
            }

        } catch (JSONException e) {
            Log.d("fadsfsads", "showResult : ", e);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

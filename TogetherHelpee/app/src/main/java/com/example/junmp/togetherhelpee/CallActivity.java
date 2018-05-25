package com.example.junmp.togetherhelpee;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static java.lang.Integer.valueOf;

public class CallActivity extends AppCompatActivity {
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    private GpsInfo gps;

    private RadioGroup radioGroup;
    private RadioButton one, two, three, four, five;
    private int checked = 1;

    private RadioGroup radioGroup2;
    private RadioButton outside, talk, housework, education;
    private String type = "outside";

    private int flag_speech = 0;

    private String date;
    private String time;
    private String etc;

    double latitude;
    double longitude;

    String dest_date;
    String dest_time;

    Intent intent;
    SpeechRecognizer mRecognizer;
    TextView textView;
    TextView textView2;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    private final int CAMERA_PERMISSIONS_GRANTED = 1;
    String phone_num;

    String mJsonString;
    getVolunteer getVolunteer;
    int haveRegisted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_call);

        Intent fromMain = getIntent();

        if(fromMain.getStringExtra("phonenum") != null){
            phone_num = (String)fromMain.getStringExtra("phonenum");
        }
        Log.d("fads",phone_num);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        String strToday = sdf.format(c1.getTime());

        String current_year = strToday.substring(0, 4);
        String current_month = strToday.substring(4, 6);
        String current_day = strToday.substring(6, 8);

        textView2.setText("오늘 날짜: "+current_month+"월 "+current_day+"일");

        getVolunteer = new getVolunteer();
        getVolunteer.execute("http://210.89.191.125/helpee/volunteers/wait/");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO
                );
            }
        }

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);




        Button btn_call = (Button) findViewById(R.id.btn_call);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecognizer.startListening(intent);

            }
        });

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

        Button btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag_speech == 0){
                    Toast.makeText(CallActivity.this,"먼저 도움요청 버튼을 누르시고 도움받으실 내용을 말씀해주세요.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(CallActivity.this, type, Toast.LENGTH_SHORT).show();
                    Toast.makeText(CallActivity.this, String.valueOf(checked), Toast.LENGTH_SHORT).show();
                    if (!isPermission) {
                        callPermission();
                        return;
                    }

                    gps = new GpsInfo(CallActivity.this);
                    // GPS 사용유무 가져오기
                    if (gps.isGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        Log.d("fdasds",String.valueOf(latitude));
                        Log.d("fdasds",String.valueOf(longitude));

                        postRequest pR = new postRequest();
                        pR.execute("http://210.89.191.125/helpee/volunteer");
/*
                        Toast.makeText(
                                getApplicationContext(),
                                "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                                Toast.LENGTH_LONG).show();*/
                    } else {
                        // GPS 를 사용할수 없으므로
                        gps.showSettingsAlert();
                    }
                }


            }
        });
        callPermission();
    }

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float v) {
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int i) {
            textView.setText("예시) 8월 17일 오전 2시에 삼성역까지 데려다 주세요");
            Toast.makeText(CallActivity.this,"너무 늦게 말하면 오류뜹니다",Toast.LENGTH_SHORT).show();
            flag_speech = 0;

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onResults(Bundle bundle) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            String message = rs[0];

            messageSeparate(message);

            if(flag_speech==1) {
                messageCheck();
            }
        }

        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };

    private void messageSeparate(String message){
        int flag = 0;
        String[] words = message.split("\\s");

        StringBuffer result = new StringBuffer();
        StringBuffer result_date = new StringBuffer();
        int temp_a = 0;
        int temp_b = 0;
        int temp_c = 0;

        int temp_m = 0;
        int temp_n = 0;

        String result_time = "";
        String result_vol = "";
        String result_day = "";

        int date_flag = 0;
        int end_flag = -1;

        for (int i = 0; i<words.length; i++){
            if (temp_m == 0 && words[i].contains("월")){
                result_date.append(words[i]);
                temp_m = 1;

                continue;
            }
            if (temp_n ==0 && words[i].contains("일")){
                result_date.append(" " + words[i]);
                temp_n = 1;

                continue;
            }
        }
        if(temp_m == 1 && temp_n == 1){
            date_flag = 1;
            result_day = result_date.toString();
        }
        if (date_flag == 0){
            textView.setText("예시) 8월 17일 오전 2시에 삼성역까지 데려다 주세요");
            Toast.makeText(CallActivity.this,"다시 한번 말씀해주세요. 날짜정보가 정확하지 않습니다.",Toast.LENGTH_SHORT).show();
        }

        for (int i=0; i<words.length; i++) {
            if (temp_a == 0 && (words[i].equals("오전") || words[i].equals("오후"))) {
                result.append(words[i]);

                temp_a = 1;
                continue;
            }
            if (temp_b == 0 && words[i].contains("시")) {
                words[i] = words[i].split("에")[0];
                result.append(" "+words[i]);
                end_flag = i;
                temp_b = 1;
                continue;
            }
            if (temp_c == 0 && (words[i].contains("시에") || words[i].contains("반에"))) {
                words[i] = words[i].split("에")[0];
                result.append(" "+words[i]);
                end_flag = i;
                temp_c = 1;
                continue;
            }
        }
        int time_flag = 0;

        if(date_flag == 1 && temp_a==1 && temp_b==1) {
            result_time = result.toString();
            time_flag = 1;
        }
        else {
            textView.setText("예시) 8월 17일 오전 2시에 삼성역까지 데려다 주세요");
            Toast.makeText(CallActivity.this,"다시 한번 말씀해주세요. 시간정보가 정확하지 않습니다.",Toast.LENGTH_SHORT).show();
        }
        if (time_flag==1) {
            int keyword_flag = -1;

            if (end_flag == words.length - 1) {
                textView.setText("예시) 8월 17일 오전 2시에 삼성역까지 데려다 주세요");
                Toast.makeText(CallActivity.this,"다시 한번 말씀해주세요. 봉사정보가 정확하지 않습니다.",Toast.LENGTH_SHORT).show();
            }
            else {
                for (int j=0; j<words.length; j++){
                    if (words[j].contains("봉사")){
                        keyword_flag = j;
                    }
                }
                if (keyword_flag == -1) {
                    keyword_flag = words.length;

                    String result_vol_temp = words[end_flag+1];
                    for(int k = end_flag+2; k<keyword_flag; k++){
                        result_vol_temp += " " + words[k];
                    }
                    result_vol = result_vol_temp;

                    String result_msg = "봉사 날짜 : " + result_day + "\n" + "봉사 시간 : " + result_time + "\n" + "봉사 종류 : " + result_vol;
                    date = result_day;
                    time = result_time;
                    etc = result_vol;
                    flag = 1;

                    textView.setText(result_msg);
                }
                else {
                    String result_vol_temp = words[end_flag+1];
                    for(int k = end_flag+2; k<keyword_flag; k++){
                        result_vol_temp += " " + words[k];
                    }

                    result_vol = result_vol_temp + " 봉사";

                    String result_msg = "봉사 날짜 : " + result_day + "\n" + "봉사 시간 : " + result_time + "\n" + "봉사 종류 : " + result_vol;
                    date = result_day;
                    time = result_time;
                    etc = result_vol;
                    flag = 1;

                    textView.setText(result_msg);
                }
            }
        }
        flag_speech = flag;
    }
    private void messageCheck(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        String strToday = sdf.format(c1.getTime());

        String current_year = strToday.substring(0, 4);
        String current_month = strToday.substring(4, 6);
        String current_day = strToday.substring(6, 8);

        String dest_year = current_year;
        String dest_month = date.split("월")[0];
        if(dest_month.equals("")){
            dest_month = current_month;
        }
        String[] dest_days = date.split(" ");
        String dest_day="";
        for(int i = 0; i<dest_days.length; i++){
            if(dest_days[i].contains("일")){
                dest_day = dest_days[i].split("일")[0];
            }
        }
        if(dest_day.equals("")){
            dest_day = current_day;
        }

        if(Integer.valueOf(dest_month) < Integer.valueOf(current_month)){
            int plusYear = Integer.valueOf(dest_year) + 1;
            dest_year = String.valueOf(plusYear);
        }
        else if(Integer.valueOf(dest_month) == Integer.valueOf(current_month) && Integer.valueOf(dest_day) < Integer.valueOf(current_day)){
            textView.setText("예시) 8월 17일 오전 2시에 삼성역까지 데려다 주세요");
            Toast.makeText(CallActivity.this, "오늘 이후의 날짜로 신청해주세요!", Toast.LENGTH_SHORT).show();
            flag_speech = 0;
        }

        dest_date = dest_year+"-"+dest_month+"-"+dest_day;

        String[] time_arr = time.split(" ");
        int am_pm = 0;
        if(time_arr[0].equals("오후")){
            am_pm = 12;
        }
        String dest_hour_str = "";
        String[] dest_hour_arr = time.split(" ");
        for(int i = 0; i<dest_hour_arr.length; i++) {
            if(dest_hour_arr[i].contains("시")){
                dest_hour_str = dest_hour_arr[i].split("시")[0];
            }
        }
        if(dest_hour_str.equals("")) {
            textView.setText("예시) 8월 17일 오전 2시에 삼성역까지 데려다 주세요");
            Toast.makeText(CallActivity.this,"시간을 정확히 말해주세요.",Toast.LENGTH_SHORT).show();
            flag_speech = 0;
        }
        int dest_hour = valueOf(dest_hour_str) + am_pm;
        int dest_min = 0;
        String dest_min_str = time_arr[time_arr.length-1];
        if(dest_min_str.contains("반")){
            dest_min = 30;
        }
        else if(dest_min_str.contains("분")){
            dest_min = valueOf(dest_min_str.split("분")[0]);
        }

        if(dest_hour<10 && dest_min<10){
            dest_time = "0"+dest_hour+":"+"0"+dest_min;
        }
        else if(dest_hour<10){
            dest_time = "0"+dest_hour+":"+dest_min;
        }
        else if(dest_min<10){
            dest_time = dest_hour+":"+"0"+dest_min;
        }
        else{
            dest_time = dest_hour+":"+dest_min;
        }

    }

    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
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
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

            Intent toCall = new Intent(CallActivity.this, CallActivity.class);
            toCall.putExtra("phonenum", phone_num);
            startActivity(toCall);
            finish();
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
                haveRegisted = 0;
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
                Log.d("Asd",startStatus+"");
                if(startStatus == 0){
                    String type = item.getString("type");
                    String helperId = item.getString("helperId");
                    int matchingStatus = item.getInt("matchingStatus");
                    String content = item.getString("content");
                    String time = item.getString("time");
                    int duration = item.getInt("duration");
                    String date = item.getString("date");
                    int volunteerId = item.getInt("volunteerId");

                    if(matchingStatus == 2){
                        Intent toStart = new Intent(CallActivity.this, StartActivity.class);
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
                    else{
                        Intent toMatch = new Intent(CallActivity.this, MatchActivity.class);
                        toMatch.putExtra("type", type);
                        toMatch.putExtra("helperId", helperId);
                        toMatch.putExtra("matchingStatus", matchingStatus);
                        toMatch.putExtra("content", content);
                        toMatch.putExtra("time", time);
                        toMatch.putExtra("duration", duration);
                        toMatch.putExtra("date", date);
                        toMatch.putExtra("volunteerId", volunteerId);
                        toMatch.putExtra("phonenum", phone_num);

                        startActivity(toMatch);

                        finish();
                    }

                }
                else if(startStatus == 1){
                    int volunteerId = item.getInt("volunteerId");
                    Intent toIng = new Intent(CallActivity.this, IngActivity.class);
                    toIng.putExtra("volunteerId", volunteerId);
                    toIng.putExtra("phonenum", phone_num);
                    startActivity(toIng);
                    finish();
                }
            }

        } catch (JSONException e) {
            Log.d("fadsfsads", "showResult : ", e);
        }
    }

}

package com.example.junmp.togetherhelpee;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.valueOf;

public class Call2Activity extends AppCompatActivity {
    private int flag_speech = 0;

    private String date;
    private String time;
    private String etc;

    String dest_date;
    String dest_time;

    Intent intent;
    SpeechRecognizer mRecognizer;

    TextView textView;
    TextView textView2;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    private final int CAMERA_PERMISSIONS_GRANTED = 1;
    String phone_num;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call2);

        Intent from = getIntent();
        phone_num = from.getStringExtra("phonenum");
        dest_date = from.getStringExtra("date");

        textView = (TextView) findViewById(R.id.textView);

        textView.setText("예시) 오후 2시 (오전, 오후를 꼭 말씀해주세요!)");
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.RECORD_AUDIO)) {

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




        ImageView btn_call = (ImageView) findViewById(R.id.btn_call);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecognizer.startListening(intent);
            }
        });

        Button btn_next = findViewById(R.id.btn_yes);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag_speech==0){
                    Toast.makeText(Call2Activity.this,"먼저 마이크 버튼을 누르시고 도움받으실 내용을 말씀해주세요.",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent toCall3 = new Intent(Call2Activity.this, Call3Activity.class);
                    toCall3.putExtra("phonenum", phone_num);
                    toCall3.putExtra("date", dest_date);
                    toCall3.putExtra("time", dest_time);
                    startActivity(toCall3);
                    finish();
                }
            }
        });

        Button btn_back = findViewById(R.id.btn_no);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(Call2Activity.this, Call1Activity.class);
                startActivity(back);
                finish();
            }
        });
    }

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            pd = new ProgressDialog(Call2Activity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("요청사항을 마이크에 대고 예시와 같이 말씀해주세요.\n예시) 오후 2시 (오전, 오후를 꼭 말씀해주세요!)");
            pd.show();
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
            pd.dismiss();
            textView.setText("예시) 오후 2시 (오전, 오후를 꼭 말씀해주세요!)");
            Toast.makeText(Call2Activity.this,"너무 늦게 말하면 오류뜹니다",Toast.LENGTH_SHORT).show();
            flag_speech = 0;

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onResults(Bundle bundle) {
            pd.dismiss();
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            String message = rs[0];

            messageSeparate(message);

            if(flag_speech==1) {
                messageCheck();

                textView.setText("\'"+dest_time+"\' 이 시간을 원하시나요?");
            }
            Toast.makeText(getApplicationContext(), textView.getText().toString(),Toast.LENGTH_LONG).show();
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

        int temp_min = 0;

        int temp_m = 0;
        int temp_n = 0;

        String result_time = "";
        String result_vol = "";
        String result_day = "";

        int date_flag = 0;
        int end_flag = -1;

        int time_flag = 0;

        for (int i=0; i<words.length; i++) {
            if(words[i].equals("자정")){
                time_flag = 1;
                result_time = "오후 12시";
                break;
            }
            if(words[i].equals("정호") || words[i].equals("정오") || words[i].equals("정우") || words[i].equals("정후")){
                time_flag = 1;
                result_time = "오전 12시";
                break;
            }
            if (temp_a == 0 && (words[i].equals("오전") || words[i].equals("오후"))) {
                result.append(words[i]);

                temp_a = 1;
                continue;
            }
            if (temp_b == 0 && words[i].contains("시")) {
                String word = words[i].split("시")[0];
                result.append(" "+word+"시");
                end_flag = i;
                temp_b = 1;
                continue;
            }
            if (temp_c == 0 && words[i].contains("반")) {
                result.append(" "+words[i]);
                end_flag = i;
                temp_c = 1;
                continue;
            }
            if (temp_c == 0 && (words[i].contains("시에") || words[i].contains("반에"))) {
                String word = words[i].split("에")[0];
                result.append(" "+word);
                end_flag = i;
                temp_c = 1;
                continue;
            }
            if (temp_min == 0 && (words[i].contains("분에") || words[i].contains("분"))) {
                String word = words[i].split("분")[0];
                result.append(" "+word+"분");
                end_flag = i;
                temp_min = 1;
                continue;
            }
        }

        if(temp_a==1 && (temp_b==1 || temp_c==1)) {
            result_time = result.toString();
            time_flag = 1;
        }
        else {
            textView.setText("예시) 오후 2시 (오전, 오후를 꼭 말씀해주세요!)");
            Toast.makeText(Call2Activity.this,"다시 한번 말씀해주세요. 시간정보가 정확하지 않습니다.",Toast.LENGTH_SHORT).show();
        }

        if (time_flag == 0){
            textView.setText("예시) 오후 2시 (오전, 오후를 꼭 말씀해주세요!)");
            Toast.makeText(Call2Activity.this,"다시 한번 말씀해주세요. 날짜정보가 정확하지 않습니다.",Toast.LENGTH_SHORT).show();
        }
        else{
            time = result_time;
        }
        flag_speech = time_flag;
    }
    private void messageCheck(){
        Log.d("afsd",time);
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
            textView.setText("예시) 오후 2시 (오전, 오후를 꼭 말씀해주세요!)");
            Toast.makeText(Call2Activity.this,"시간을 정확히 말해주세요.",Toast.LENGTH_SHORT).show();
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

}

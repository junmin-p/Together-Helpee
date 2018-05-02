package com.example.junmp.togetherhelpee;

import android.Manifest;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CallActivity extends AppCompatActivity {

    Intent intent;
    SpeechRecognizer mRecognizer;
    TextView textView;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_call);

        textView = (TextView) findViewById(R.id.textView);
        textView.setText("예시) 오전 2시에 삼성역까지 데려다 주세요");


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




        Button button = (Button) findViewById(R.id.btn_call);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecognizer.startListening(intent);

            }
        });

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
            textView.setText("너무 늦게 말하면 오류뜹니다");

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
        }

        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };

    private void messageSeparate(String message){
        String[] words = message.split("\\s");

        StringBuffer result = new StringBuffer();

        int temp_a = 0;
        int temp_b = 0;
        int temp_c = 0;

        String result_time = "";
        String result_vol = "";

        int end_flag = -1;

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
            if (temp_c == 0 && (words[i].contains("에") || words[i].contains("반"))) {
                words[i] = words[i].split("에")[0];
                result.append(" "+words[i]);
                end_flag = i;
                temp_c = 1;
                continue;
            }
        }
        int time_flag = 0;

        if(temp_a==1 && temp_b==1) {
            result_time = result.toString();
            time_flag = 1;
        }
        else {
            Toast.makeText(CallActivity.this,"다시 한번 말씀해주세요. 시간정보가 정확하지 않습니다.",Toast.LENGTH_SHORT).show();
        }
        if (time_flag==1) {
            int keyword_flag = -1;

            if (end_flag == words.length - 1) {
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

                    String result_msg = "봉사 시간 : " + result_time + "\n" + "봉사 종류 : " + result_vol;

                    textView.setText(result_msg);
                }
                else {
                    String result_vol_temp = words[end_flag+1];
                    for(int k = end_flag+2; k<keyword_flag; k++){
                        result_vol_temp += " " + words[k];
                    }

                    result_vol = result_vol_temp + " 봉사";

                    String result_msg = "봉사 시간 : " + result_time + "\n" + "봉사 종류 : " + result_vol;

                    textView.setText(result_msg);
                }
            }
        }
    }
}

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Integer.valueOf;

public class Call3Activity extends AppCompatActivity {
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
        setContentView(R.layout.activity_call3);

        Intent from = getIntent();
        phone_num = from.getStringExtra("phonenum");
        dest_date = from.getStringExtra("date");
        dest_time = from.getStringExtra("time");

        textView = (TextView) findViewById(R.id.textView);

        textView.setText("예시) 삼성역까지 데려다 주세요");
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
                    Toast.makeText(Call3Activity.this,"먼저 마이크 버튼을 누르시고 도움받으실 내용을 말씀해주세요.",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent toCall4 = new Intent(Call3Activity.this, Call4Activity.class);
                    toCall4.putExtra("phonenum", phone_num);
                    toCall4.putExtra("etc", etc);
                    toCall4.putExtra("date", dest_date);
                    toCall4.putExtra("time", dest_time);
                    startActivity(toCall4);
                    finish();
                }
            }
        });

        Button btn_back = findViewById(R.id.btn_no);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(Call3Activity.this, Call2Activity.class);
                back.putExtra("phonenum",phone_num);
                back.putExtra("date",dest_date);
                startActivity(back);
                finish();
            }
        });
    }

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            pd = new ProgressDialog(Call3Activity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("요청사항을 마이크에 대고 예시와 같이 말씀해주세요.\n예시) 삼성역까지 데려다 주세요");
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
            textView.setText("예시) 삼성역까지 데려다 주세요");
            Toast.makeText(Call3Activity.this,"너무 늦게 말하면 오류뜹니다",Toast.LENGTH_SHORT).show();
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
        if(message.equals("")){
            flag_speech = 0;
        }
        else{
            etc = message;

            flag_speech = 1;

            textView.setText("\'"+etc+"\' 이것을 원하시나요?");
        }
    }

}

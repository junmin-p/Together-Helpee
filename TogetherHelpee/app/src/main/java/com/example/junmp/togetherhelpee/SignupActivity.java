package com.example.junmp.togetherhelpee;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    ImageView img_preview;
    Button btn_signup;

    Bitmap bmp_preview;

    String phone_num;
    String deviceKey;

    String profile_url;
    Bitmap bitmap;

    ImageView btn_name;
    TextView txt_name;


    Intent intent;
    SpeechRecognizer mRecognizer;

    ProgressDialog pd;

    int flag = 0;

    putAttr putAttr;

    String name;
    String age;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btn_signup = findViewById(R.id.btn_signup);
        img_preview = findViewById(R.id.img_preview);
        btn_name = findViewById(R.id.btn_name);
        txt_name = findViewById(R.id.txt_name);

        Toast.makeText(getApplicationContext(), "사진을 다시찍으려면 우측상단의 버튼을, 그대로 가입하시려면 하단의 가입버튼을 눌러주세요.", Toast.LENGTH_LONG).show();

        final Spinner yearSpinner = (Spinner)findViewById(R.id.btn_age);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.age, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        Intent intent1 = getIntent();
        if(intent1.getStringExtra("url") != null){
            profile_url = intent1.getStringExtra("url");
            File imgFile = new  File(profile_url);
            if(imgFile.exists()){

                Log.d("fadsfsads", imgFile.getAbsolutePath());
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                img_preview.setImageBitmap(myBitmap);

            }
        }
        if(intent1.getStringExtra("phonenum") != null){
            phone_num = intent1.getStringExtra("phonenum");
        }
        if(intent1.getStringExtra("deviceKey") != null){
            deviceKey = intent1.getStringExtra("deviceKey");
        }


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((yearSpinner.getSelectedItem().toString()).equals("100세 이상")){
                    age = "100";
                }
                else if((yearSpinner.getSelectedItem().toString()).equals("10세 이하")){
                    age = "0";
                }
                else{
                    age = yearSpinner.getSelectedItem().toString().substring(0,2);
                }
                if(name.equals(null)){
                    Toast.makeText(getApplicationContext(),"마이크 버튼을 누르고 이름을 말해주세요.",Toast.LENGTH_SHORT).show();
                }
                else if(age.equals(null)){
                    Toast.makeText(getApplicationContext(), "연령 버튼을 누르고 연령대를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    putAttr = new putAttr();
                    putAttr.execute("http://210.89.191.125/helpee/name-age");
                }
            }
        });

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);

        btn_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 0;
                mRecognizer.startListening(intent);
            }
        });

    }
    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            if(flag == 0){
                pd = new ProgressDialog(SignupActivity.this);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setMessage("이름을 말씀해주세요.");
                pd.show();
            }
            else{
                pd = new ProgressDialog(SignupActivity.this);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setMessage("다시 한번 말씀해주세요.");
                pd.show();
            }
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
            flag = 1;
            pd.dismiss();
            mRecognizer.startListening(intent);
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


            txt_name.setText(message+"님 환영합니다.");
            name = message;
        }

        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };

    class putAttr extends AsyncTask<String, Void, String> {
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Intent toCall = new Intent(SignupActivity.this, Call1Activity.class);
            startActivity(toCall);
            finish();
        }

        @Override
        protected String doInBackground(String... params) {
            String UPLOAD_URL = params[0];
            HashMap<String, String> data = new HashMap<>();
            data.put("name", name);
            data.put("age", age);
            data.put("userPhone", phone_num);

            String result = rh.sendPutRequest(UPLOAD_URL, data);

            return result;
        }
    }
}

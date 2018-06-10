package com.example.junmp.togetherhelpee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {
    String phone_num;
    String from;
    String device_Key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Intent fromWhere = getIntent();
        if(fromWhere.getStringExtra("phonenum")!=null){
            phone_num = fromWhere.getStringExtra("phonenum");
        }
        if(fromWhere.getStringExtra("from").equals("first")){
            from = "first";
        }
        else if(fromWhere.getStringExtra("from").equals("re")){
            from = "re";
        }
        if(fromWhere.getStringExtra("deviceKey")!=null){
            device_Key = fromWhere.getStringExtra("deviceKey");
        }

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(SplashActivity.this, FaceActivity.class);
                    intent.putExtra("from", "first");
                    intent.putExtra("phonenum", phone_num);
                    intent.putExtra("deviceKey",device_Key);
                    startActivity(intent);
                }
            }
        };
        timer.start();
    }
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}

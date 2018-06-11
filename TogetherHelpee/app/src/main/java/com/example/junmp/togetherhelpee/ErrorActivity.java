package com.example.junmp.togetherhelpee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_error);/*
        Toast.makeText(getApplicationContext(),"네트워크 오류입니다. 네트워크 상태를 확인해주세요!",Toast.LENGTH_LONG).show();*/
    }
}

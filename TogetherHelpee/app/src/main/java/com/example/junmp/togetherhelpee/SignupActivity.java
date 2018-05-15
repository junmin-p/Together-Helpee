package com.example.junmp.togetherhelpee;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    TextView text_phonenumber;
    Button btn_recapture;
    ImageView img_preview;
    Button btn_signup;

    Bitmap bmp_preview;

    String phone_num;

    String profile_url;
    Bitmap bitmap;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        text_phonenumber = findViewById(R.id.text_phonenumber);
        btn_recapture = findViewById(R.id.btn_recapture);
        btn_signup = findViewById(R.id.btn_signup);
        img_preview = findViewById(R.id.img_preview);

        text_phonenumber.setText(phone_num);

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

        btn_recapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recapture = new Intent(SignupActivity.this, FaceActivity.class);
                recapture.putExtra("from","re");
                recapture.putExtra("phonenum", phone_num);
                startActivity(recapture);
                finish();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toMain = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(toMain);
                finish();
            }
        });

    }
}

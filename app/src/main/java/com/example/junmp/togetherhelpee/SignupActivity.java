package com.example.junmp.togetherhelpee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.junmp.togetherhelpee.activity.user.register.form.CameraActivity;

import java.io.File;

public class SignupActivity extends AppCompatActivity {
    TextView text_phonenumber;
    Button btn_recapture;
    ImageView img_preview;
    Button btn_signup;

    Bitmap bmp_preview;

    String phone_num;
    String deviceKey;

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

        Toast.makeText(getApplicationContext(), "사진을 다시찍으려면 우측상단의 버튼을, 그대로 가입하시려면 하단의 가입버튼을 눌러주세요.", Toast.LENGTH_LONG).show();

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

        btn_recapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recapture = new Intent(SignupActivity.this, CameraActivity.class);
                recapture.putExtra("from","re");
                recapture.putExtra("phonenum", phone_num);
                recapture.putExtra("deviceKey", deviceKey);
                startActivity(recapture);
                finish();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toCall = new Intent(SignupActivity.this, CallActivity.class);
                toCall.putExtra("phonenum",phone_num);
                startActivity(toCall);
                finish();
            }
        });

    }
}

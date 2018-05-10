package com.example.junmp.togetherhelpee;

import android.Manifest;
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
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    public static final String UPLOAD_URL = "http://192.168.17.15:9001/helpee/test";

    TextView text_phonenumber;
    Button btn_recapture;
    ImageView img_preview;
    Button btn_signup;

    UploadImage uploadimage;

    Bitmap bmp_preview;

    String phone_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        text_phonenumber = findViewById(R.id.text_phonenumber);
        btn_recapture = findViewById(R.id.btn_recapture);
        btn_signup = findViewById(R.id.btn_signup);
        img_preview = findViewById(R.id.img_preview);


        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        String PhoneNum = telManager.getLine1Number();
        if(PhoneNum.startsWith("+82")){
            PhoneNum = PhoneNum.replace("+82", "0");
        }
        phone_num = PhoneNum;

        text_phonenumber.setText(PhoneNum);

        Intent intent1 = getIntent();
        if(intent1.getStringExtra("data") != null){

            String bm = (String)intent1.getStringExtra("data");

            try {
                File f=new File(bm, "profile.jpg");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                bmp_preview = b;
                img_preview.setImageBitmap(b);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

            Log.d("Fadsfsaf","Afsdaf");
        }

        btn_recapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recapture = new Intent(SignupActivity.this, FaceActivity.class);
                startActivity(recapture);
                finish();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadimage = new UploadImage();
                uploadimage.execute(bmp_preview);

                Intent toMain = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(toMain);
                finish();
            }
        });

    }
    class UploadImage extends AsyncTask<Bitmap, Void, String> {
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap bitmap = params[0];
            String uploadImage = getStringImage(bitmap);
            HashMap<String, String> data = new HashMap<>();

            data.put("id", "보웡이");
            data.put("user_phone", phone_num);
            data.put("img", uploadImage);

            Log.d("img",uploadImage);
            String result = rh.sendPostRequest(UPLOAD_URL, data);

            return result;
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bmp_temp = Bitmap.createScaledBitmap(bmp, 200, 200, true);
        bmp_temp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}

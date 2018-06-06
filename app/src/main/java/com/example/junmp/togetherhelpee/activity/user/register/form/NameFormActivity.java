package com.example.junmp.togetherhelpee.activity.user.register.form;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.junmp.togetherhelpee.R;

import java.util.ArrayList;


/**
 * TODO
 * 음성이 인식되면 mic 이미지에 바운스 애니메이션 줄것 ( 옵션 )
 * 이름이 인식되면 확인 절차 없이 다음 activity 로 진입한다.
 * 이전 액티비티에서 전달된 사진 정보 ( path or url ) 도 같이 다음 activity 로 전달한다.
 *
 */
public class NameFormActivity extends AppCompatActivity {
    private String name;
    private static final int RESULT_SPEECH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form_name);
        final String imageName = getIntent().getStringExtra("imageName");

        Button btnDummyNext = findViewById(R.id.btn_next);

        btnDummyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name == null) {
                    Toast.makeText(NameFormActivity.this , "마이크를 클릭하고 이름을 말해주세요" , Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent registerIntent = new Intent(NameFormActivity.this, AgeFormActivity.class);


                registerIntent.putExtra("imageName" , imageName);
                registerIntent.putExtra("name" , name);

                startActivity(registerIntent);
                finish();
            }
        });

        ImageView micView = findViewById(R.id.img_mic);

        micView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent micIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                micIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE , getPackageName());
                micIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE , "ko-KR");
                micIntent.putExtra(RecognizerIntent.EXTRA_PROMPT , "말해주세요");

                startActivityForResult(micIntent , RESULT_SPEECH);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == RESULT_SPEECH)) {
            ArrayList<String> sttResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (sttResults.isEmpty()) {
                Toast.makeText(NameFormActivity.this , "다시 말해 주세요" , Toast.LENGTH_SHORT).show();
            } else {
                name = sttResults.get(0);
            }
        }
    }
}
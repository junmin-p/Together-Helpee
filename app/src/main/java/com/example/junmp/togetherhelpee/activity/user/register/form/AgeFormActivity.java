package com.example.junmp.togetherhelpee.activity.user.register.form;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.volunteer.request.form.WhenFormActivity;
import com.example.junmp.togetherhelpee.domain.user.UserService;


/**
 * TODO
 * 음성이 인식되면 mic 이미지에 바운스 애니메이션 줄것 ( 옵션 )
 * 나이가 인식되면 이전 액티비티에서 전달된 사진 정보 ( path or url ) 와 이름 나이를 서버에 전송하여 회원 가입 절차를 실행할것
 *
 */
public class AgeFormActivity extends AppCompatActivity {
    private String age = "예순다섯살";
    private UserService userService = new UserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form_age);
        final String referer = getIntent().getStringExtra("nextActivity");
        final String imageUrl = getIntent().getStringExtra("imageUrl");
        final String name = getIntent().getStringExtra("name");


        Button btnDummyNext = findViewById(R.id.btn_next);

        btnDummyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(AgeFormActivity.this, GenderFormActivity.class);

                if (hasNextAndWhenForm(referer))
                    registerIntent.putExtra("nextActivity", referer);


                startActivity(registerIntent);
                finish();
            }
        });
    }

    private boolean hasNextAndWhenForm(String referer) {
        return referer != null && referer.equals(WhenFormActivity.class.getSimpleName());
    }
}
package com.example.junmp.togetherhelpee.activity.user.register.form;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.volunteer.request.form.WhenFormActivity;



/**
 * TODO
 * 음성이 인식되면 mic 이미지에 바운스 애니메이션 줄것 ( 옵션 )
 * 이름이 인식되면 확인 절차 없이 다음 activity 로 진입한다.
 * 이전 액티비티에서 전달된 사진 정보 ( path or url ) 도 같이 다음 activity 로 전달한다.
 *
 */
public class NameWithVoiceActivity extends AppCompatActivity {
    private String name = "홍길동";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_name);
        final String referer = getIntent().getStringExtra("nextActivity");
        final String imageUrl = getIntent().getStringExtra("imageUrl");

        Button btnDummyNext = findViewById(R.id.btn_next);

        btnDummyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(NameWithVoiceActivity.this, AgeWithVoiceActivity.class);

                if (hasNextAndWhenForm(referer))
                    registerIntent.putExtra("nextActivity", referer);

                registerIntent.putExtra("imageUrl" , imageUrl);
                registerIntent.putExtra("name" , name);

                startActivity(registerIntent);
                finish();
            }
        });
    }

    private boolean hasNextAndWhenForm(String referer) {
        return referer != null && referer.equals(WhenFormActivity.class.getSimpleName());
    }
}
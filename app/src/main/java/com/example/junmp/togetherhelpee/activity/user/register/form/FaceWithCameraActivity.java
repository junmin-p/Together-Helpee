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
 * 얼굴 인식후 사진을 자동으로 촬영한다.
 * 촬영된 사진 리소스는 다음 activity 에 전달한다.
 * 혹은 즉시 서버에 업로드 한 이후 url 을 다음 activity 에 전달한다.
 * 사진을 서버에 업로드 할때 progress bar 를 구현해야 한다. ( 옵션 )
 */
public class FaceWithCameraActivity extends AppCompatActivity {
    private String imageUrl = "http://210.89.191.125/photo/1527532115221.jpeg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_face);
        final String referer = getIntent().getStringExtra("nextActivity");
        Button btnDummyNext = findViewById(R.id.btn_next);

        btnDummyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(FaceWithCameraActivity.this, NameWithVoiceActivity.class);

                if (hasNextAndWhenForm(referer))
                    registerIntent.putExtra("nextActivity", referer);

                // 추출한 사진 정보 같이 전달할것 ( path or url )
                registerIntent.putExtra("imageUrl", imageUrl);


                startActivity(registerIntent);
                finish();
            }
        });
    }

    private boolean hasNextAndWhenForm(String referer) {
        return referer != null && referer.equals(WhenFormActivity.class.getSimpleName());
    }
}

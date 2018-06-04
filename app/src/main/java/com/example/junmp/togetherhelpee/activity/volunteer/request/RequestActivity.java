package com.example.junmp.togetherhelpee.activity.volunteer.request;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.register.form.FaceWithActivity;
import com.example.junmp.togetherhelpee.activity.volunteer.request.form.WhenFormActivity;
import com.example.junmp.togetherhelpee.domain.user.UserService;

public class RequestActivity extends AppCompatActivity {
    private UserService userService = new UserService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_request);

        Button btnRequestVolunteer = findViewById(R.id.request_volunteer);

        btnRequestVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 만약 회원 가입하지 않았다면 회원 가입 부터 시킨다.
                 * 회원 가입 완료 이후 도움 요청 Activity 로 진입한다.
                 */
                if (userService.isNotRegistered()) {
                    Intent registerIntent = new Intent(RequestActivity.this, FaceWithActivity.class);
                    registerIntent.putExtra("nextActivity" , WhenFormActivity.class.getSimpleName());
                    startActivity(registerIntent);

                } else {
                    startActivity(new Intent(RequestActivity.this, WhenFormActivity.class));
                }
            }
        });
    }
}

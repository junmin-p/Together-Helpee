package com.example.junmp.togetherhelpee.activity.user.register.form;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.volunteer.request.form.VolunteerFormActivity;
import com.example.junmp.togetherhelpee.domain.user.UserService;

public class ConfirmActivity extends AppCompatActivity {
    private String age = "예순다섯살";
    private UserService userService = new UserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_confirm);
        final String imageUrl = getIntent().getStringExtra("imageUrl");
        final String name = getIntent().getStringExtra("name");


        Button btnDummyNext = findViewById(R.id.btn_next);

        btnDummyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(ConfirmActivity.this, VolunteerFormActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
    }
}

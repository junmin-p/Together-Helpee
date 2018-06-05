package com.example.junmp.togetherhelpee.activity.user.register.form;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.home.HomeActivity;
import com.example.junmp.togetherhelpee.activity.volunteer.request.form.WhenFormActivity;
import com.example.junmp.togetherhelpee.domain.user.UserService;

public class ConfirmActivity extends AppCompatActivity {
    private String age = "예순다섯살";
    private UserService userService = new UserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_confirm);
        final String referer = getIntent().getStringExtra("nextActivity");
        final String imageUrl = getIntent().getStringExtra("imageUrl");
        final String name = getIntent().getStringExtra("name");


        Button btnDummyNext = findViewById(R.id.btn_next);

        btnDummyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (hasNextAndWhenForm(referer)) {
                    Intent registerIntent = new Intent(ConfirmActivity.this, WhenFormActivity.class);
                    startActivity(registerIntent);
                } else {
                    Intent registerIntent = new Intent(ConfirmActivity.this, HomeActivity.class);
                    startActivity(registerIntent);
                }

                finish();
            }
        });
    }

    private boolean hasNextAndWhenForm(String referer) {
        return referer != null && referer.equals(WhenFormActivity.class.getSimpleName());
    }
}

package com.example.junmp.togetherhelpee.activity.volunteer.request.form;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.volunteer.recommend.RecommendActivity;
import com.example.junmp.togetherhelpee.domain.user.UserService;
import com.example.junmp.togetherhelpee.domain.volunteer.VolunteerForm;
import com.example.junmp.togetherhelpee.domain.volunteer.VolunteerService;

import java.util.Date;

public class WhatFormActivity extends AppCompatActivity {

    private UserService userService = new UserService();
    private VolunteerService volunteerService = new VolunteerService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_form_what);

        Button btnDummyNext = findViewById(R.id.btn_next);

        btnDummyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*volunteerService.getRecommendHelper(new VolunteerForm());*/

                Intent registerIntent = new Intent(WhatFormActivity.this, RecommendActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
    }
}
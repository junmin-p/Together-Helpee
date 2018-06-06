package com.example.junmp.togetherhelpee.activity.volunteer.matched.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.home.HomeActivity;
import com.example.junmp.togetherhelpee.domain.user.UserService;
import com.example.junmp.togetherhelpee.domain.volunteer.VolunteerService;

public class MatchedStartActivity extends AppCompatActivity {

    private UserService userService = new UserService();
    private VolunteerService volunteerService = new VolunteerService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_matched_start);

        Button btnCallPhone = findViewById(R.id.btn_call_phone);

        btnCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        Button btnGohome = findViewById(R.id.btn_home);

        btnGohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*volunteerService.getRecommendHelper(new VolunteerForm());*/

                Intent registerIntent = new Intent(MatchedStartActivity.this, HomeActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
    }
}
package com.example.junmp.togetherhelpee.activity.volunteer.request.form;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.home.HomeActivity;
import com.example.junmp.togetherhelpee.activity.volunteer.recommend.RecommendActivity;

public class RequestDoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_request_done);

        Button button = findViewById(R.id.btn_home);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RequestDoneActivity.this , HomeActivity.class));
                finish();
            }
        });
    }

}

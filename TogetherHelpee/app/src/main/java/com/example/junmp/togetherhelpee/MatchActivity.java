package com.example.junmp.togetherhelpee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Intent fromCall = getIntent();
        String type = fromCall.getStringExtra("type");
        String helperId = fromCall.getStringExtra("helperId");
        int matchingStatus = fromCall.getIntExtra("matchingStatus", 0);
        String content = fromCall.getStringExtra("content");
        String time = fromCall.getStringExtra("time");
        int duration = fromCall.getIntExtra("duration", 0);
        String json = fromCall.getStringExtra("date");
        TextView tv = findViewById(R.id.json);
        tv.setText(content);
    }
}

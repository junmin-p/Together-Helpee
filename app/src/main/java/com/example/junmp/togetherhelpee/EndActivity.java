package com.example.junmp.togetherhelpee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EndActivity extends AppCompatActivity {
    Button btn_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Toast.makeText(getApplicationContext(), "봉사를 성공적으로 받으셨군요? 이용해주셔서 감사합니다. " +
                "최하단의 버튼을 누르시면 메인화면으로 돌아가 새로운 도움을 요청하실 수 있습니다.", Toast.LENGTH_LONG).show();

        btn_home = findViewById(R.id.btn_home);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMain = new Intent(EndActivity.this, MainActivity.class);
                startActivity(toMain);
                finish();
            }
        });
    }
}

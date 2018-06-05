package com.example.junmp.togetherhelpee.activity.user.register.form;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.home.DisplayHome;
import com.example.junmp.togetherhelpee.activity.volunteer.request.form.WhenFormActivity;
import com.example.junmp.togetherhelpee.common.util.DeviceUUIDFactory;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUtil;
import com.example.junmp.togetherhelpee.domain.user.User;
import com.example.junmp.togetherhelpee.domain.user.UserService;
import com.example.junmp.togetherhelpee.domain.volunteer.Volunteer;


public class AgeFormActivity extends AppCompatActivity {

    private UserService userService = new UserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form_age);
        final String referer = getIntent().getStringExtra("nextActivity");
        final String imageName = getIntent().getStringExtra("imageName");
        final String name = getIntent().getStringExtra("name");
        final EditText editText = findViewById(R.id.age);



        Button btnDummyNext = findViewById(R.id.btn_next);

        btnDummyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int age = Integer.parseInt(editText.getText().toString());
                if (age == 0) {
                    Toast.makeText(AgeFormActivity.this , "나이를 입력해 주세요." , Toast.LENGTH_SHORT).show();
                    return;
                }

                new AsyncInit().execute(String.valueOf(age) , name , imageName);
            }
        });
    }

    private class AsyncInit extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            int age = Integer.parseInt(params[0]);
            String name = params[1];
            String imageName = params[2];

            userService.register(age , name , imageName , new DeviceUUIDFactory(getApplicationContext()).getDeviceUuid() , DeviceUtil.getPhoneNumber(AgeFormActivity.this));

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            startActivity(new Intent(AgeFormActivity.this , ConfirmActivity.class));
            finish();
        }
    }
}
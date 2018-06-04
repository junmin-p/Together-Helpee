package com.example.junmp.togetherhelpee.activity.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.home.MainActivity;
import com.example.junmp.togetherhelpee.activity.volunteer.request.RequestActivity;
import com.example.junmp.togetherhelpee.domain.user.User;
import com.example.junmp.togetherhelpee.domain.user.UserService;
import com.example.junmp.togetherhelpee.domain.volunteer.Volunteer;
import com.example.junmp.togetherhelpee.domain.volunteer.VolunteerService;

public class SplashActivity extends AppCompatActivity {
    private UserService userService = new UserService();
    private VolunteerService volunteerService = new VolunteerService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    Intent intent = null;

                    if (userService.isLogged()) {
                        User user = userService.getLoggedUser("test");

                        Volunteer volunteer = volunteerService.getOne(user.getId());

                        if (volunteer == null) {
                            intent = new Intent(SplashActivity.this, RequestActivity.class);
                        } else if (volunteer.isStandBy()) {

                        }

                    } else {
                        intent = new Intent(SplashActivity.this, RequestActivity.class);
                    }

                    startActivity(intent);
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

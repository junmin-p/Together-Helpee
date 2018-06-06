package com.example.junmp.togetherhelpee.activity.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.user.register.form.FaceFormActivity;
import com.example.junmp.togetherhelpee.activity.volunteer.request.form.VolunteerFormActivity;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUtil;
import com.example.junmp.togetherhelpee.domain.user.User;
import com.example.junmp.togetherhelpee.domain.user.UserService;
import com.example.junmp.togetherhelpee.domain.volunteer.Volunteer;
import com.example.junmp.togetherhelpee.domain.volunteer.VolunteerService;

public class HomeActivity extends AppCompatActivity {
    private UserService userService = new UserService();
    private VolunteerService volunteerService = new VolunteerService();
    private DisplayHome displayHome;
    private TextView titleView;
    private TextView volunteerView;
    private Button requestHelpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        titleView = findViewById(R.id.title);
        volunteerView = findViewById(R.id.volunteer);
        requestHelpButton = findViewById(R.id.request);

        requestHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (displayHome.getUser() == null) {
                    Intent registerIntent = new Intent(HomeActivity.this, FaceFormActivity.class);
                    startActivity(registerIntent);

                } else {
                    startActivity(new Intent(HomeActivity.this, VolunteerFormActivity.class));
                }

            }
        });

        new AsyncInit().execute();
    }


    private class AsyncInit extends AsyncTask<String, Void, DisplayHome> {
        @Override
        protected DisplayHome doInBackground(String... params) {
            DisplayHome home = new DisplayHome();

            User user = userService.getLoggedUser(DeviceUtil.getPhoneNumber(HomeActivity.this));
            home.setUser(user);

            if (user != null) {
                Volunteer volunteer = volunteerService.getActiveOne(user.getId());
                home.setVolunteer(volunteer);
            }

            return home;
        }

        @Override
        protected void onPostExecute(DisplayHome home) {
            displayHome = home;

            if (home.getUser() != null) {
                User user = home.getUser();
                String title = titleView.getText().toString();
                String replace = title.replace(":name", user.getName()).replace(":gender", user.getGender());
                titleView.setText(replace);
                titleView.setVisibility(View.VISIBLE);
            }

            if (home.getVolunteer() != null) {
                Volunteer volunteer = home.getVolunteer();
                String title = volunteerView.getText().toString();
                String replace = title.replace(":createdAt", volunteer.getCreatedAt().toString()).replace(":content", volunteer.getContent());
                volunteerView.setText(replace);
                volunteerView.setVisibility(View.VISIBLE);
                requestHelpButton.setVisibility(View.GONE);
            }

        }
    }
}

package com.example.junmp.togetherhelpee.activity.volunteer.request.form;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.junmp.togetherhelpee.GpsInfo;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.volunteer.recommend.RecommendActivity;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUtil;
import com.example.junmp.togetherhelpee.common.util.text.TextUtil;
import com.example.junmp.togetherhelpee.domain.volunteer.Volunteer;
import com.example.junmp.togetherhelpee.domain.volunteer.VolunteerForm;
import com.example.junmp.togetherhelpee.domain.volunteer.VolunteerService;
import com.example.junmp.togetherhelpee.domain.volunteer.helper.Helper;

import java.util.ArrayList;
import java.util.Date;

public class VolunteerFormActivity extends AppCompatActivity {

    private VolunteerService volunteerService = new VolunteerService();
    private static final int REQUEST_START_AT = 1;
    private static final int REQUEST_END_AT = 2;
    private static final int REQUEST_MESSAGE = 3;
    private static final int REQUEST_SEND = 4;
    private int currentStep = 1;
    private VolunteerForm form;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private Intent micIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_feedback);
        micIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        micIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        micIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        Button btnDummyNext = findViewById(R.id.btn_next);
        btnDummyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (form.validate()) {
                    new AsyncRegister().execute(form);
                } else {
                    Toast.makeText(getApplicationContext(), form.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        ImageView micView = findViewById(R.id.img_mic);
        micView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (currentStep) {
                    case REQUEST_START_AT: {
                        micIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "언제부터 도와 드릴까요? ( 예 : 내일 오전 여섯시 , 모레 오후 일곱시 ) ");
                        startActivityForResult(micIntent, REQUEST_START_AT);
                        break;
                    }
                    case REQUEST_END_AT: {
                        micIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "언제까지 도와 드릴까요? ( 예 : 내일 오전 여섯시 , 모레 오후 일곱시 ) ");
                        startActivityForResult(micIntent, REQUEST_END_AT);
                        break;
                    }
                    case REQUEST_MESSAGE: {
                        micIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "무엇을 도와 드릴까요?");
                        startActivityForResult(micIntent, REQUEST_MESSAGE);
                        break;
                    }
                }
            }
        });

        Location location = getMyLocation();

        if (location != null) {
            form = new VolunteerForm(DeviceUtil.getPhoneNumber(VolunteerFormActivity.this), location.getLatitude(), location.getLongitude());
        }
    }


    private class AsyncRegister extends AsyncTask<VolunteerForm, Void, Helper> {
        @Override
        protected Helper doInBackground(VolunteerForm... param) {
            Volunteer saved = volunteerService.save(param[0]);
            Helper recommendHelper = volunteerService.findRecommendHelper(saved.getVolunteerId());
            return recommendHelper;
        }

        @Override
        protected void onPostExecute(Helper helper) {
            if (helper != null) {
                Intent intent = new Intent(VolunteerFormActivity.this, RecommendActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(VolunteerFormActivity.this, RequestDoneActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private Location getMyLocation() {
        Location currentLocation = null;
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 사용자 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_ACCESS_FINE_LOCATION);
        } else {
            GpsInfo gpsInfo = new GpsInfo(VolunteerFormActivity.this);
            currentLocation = gpsInfo.getLocation();
        }
        return currentLocation;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        ArrayList<String> sttResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

        if (sttResults.isEmpty()) {
            Toast.makeText(VolunteerFormActivity.this, "다시 말해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == REQUEST_START_AT) {
            Date startAt = TextUtil.textToDate(sttResults.get(0));

            if (startAt != null) {
                form.setStartAt(startAt);
                currentStep = REQUEST_END_AT;
            } else {
                //retry
            }

        } else if (requestCode == REQUEST_END_AT) {
            Date endAt = TextUtil.textToDate(sttResults.get(0));

            if (endAt != null) {
                form.setEndAt(endAt);
                currentStep = REQUEST_MESSAGE;
            } else {
                //retry
            }
        } else if (requestCode == REQUEST_MESSAGE) {
            form.setMessage(sttResults.get(0));
            currentStep = REQUEST_SEND;
        }
    }
}
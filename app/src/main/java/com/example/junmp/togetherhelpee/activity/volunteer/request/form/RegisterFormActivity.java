package com.example.junmp.togetherhelpee.activity.volunteer.request.form;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.junmp.togetherhelpee.GpsInfo;
import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.common.AbstractWebViewActivity;
import com.example.junmp.togetherhelpee.activity.volunteer.recommend.RecommendActivity;
import com.example.junmp.togetherhelpee.common.constante.Server;
import com.example.junmp.togetherhelpee.common.util.device.DeviceUtil;
import com.example.junmp.togetherhelpee.common.util.text.ParseDateException;
import com.example.junmp.togetherhelpee.common.util.text.ParseTimeException;
import com.example.junmp.togetherhelpee.common.util.text.TextParser;
import com.example.junmp.togetherhelpee.domain.user.User;
import com.example.junmp.togetherhelpee.domain.user.UserService;
import com.example.junmp.togetherhelpee.domain.volunteer.Volunteer;
import com.example.junmp.togetherhelpee.domain.volunteer.VolunteerForm;
import com.example.junmp.togetherhelpee.domain.volunteer.VolunteerService;
import com.example.junmp.togetherhelpee.domain.volunteer.helper.Helper;

import java.util.ArrayList;
import java.util.Date;

public class RegisterFormActivity extends AbstractWebViewActivity {

    private UserService userService = new UserService();
    private VolunteerService volunteerService = new VolunteerService();
    private static final int REQUEST_START_AT = 1;

    private static final int REQUEST_END_AT = 2;
    private static final int REQUEST_MESSAGE = 3;

    private int currentStep = REQUEST_START_AT;
    private static boolean IS_RETRY_TIME = false;
    private static boolean IS_RETRY_DATE = false;
    private static  final int REQUEST_DONE = 4;

    private VolunteerForm form;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private Intent micIntent;
    private TextParser textParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_register_webview);
        super.initWebview(R.id.webview);
        micIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        micIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        micIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        new AsyncGetUser().execute(DeviceUtil.getPhoneNumber(RegisterFormActivity.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentStep == REQUEST_START_AT && IS_RETRY_DATE) {
            showMic("다시 한번 이야기 해 주세요.  오늘 , 내일 처럼 자세히 부탁드려요", REQUEST_START_AT);
        } else if (currentStep == REQUEST_START_AT && IS_RETRY_TIME) {
            showMic("다시 한번 이야기 해 주세요.  한시 , 두시 처럼 자세히 부탁드려요", REQUEST_START_AT);
        } else if (currentStep == REQUEST_END_AT && IS_RETRY_TIME == false && IS_RETRY_DATE == false) {
            showMic("언제까지 도와 드릴까요? ( 예 : 내일 오전 여섯시 , 모레 오후 일곱시 )", REQUEST_END_AT);
        } else if (currentStep == REQUEST_END_AT && IS_RETRY_DATE) {
            showMic("다시 한번 이야기 해 주세요.  오늘 , 내일 처럼 자세히 부탁드려요", REQUEST_END_AT);
        } else if (currentStep == REQUEST_END_AT && IS_RETRY_TIME) {
            showMic("다시 한번 이야기 해 주세요.  한시 , 두시 처럼 자세히 부탁드려요", REQUEST_END_AT);
        } else if (currentStep == REQUEST_MESSAGE) {
            showMic("무엇을 도와드릴까요?", REQUEST_MESSAGE);
        } else if (currentStep == REQUEST_DONE) {
            new AsyncRegister().execute(form);
        }
    }

    private void bindJavascript() {
        webView.addJavascriptInterface(new Object() {

            @JavascriptInterface
            public void clickMic() {
                showMic("언제부터 도와 드릴까요?  ( 예 : 내일 오전 여섯시 , 모레 오후 일곱시 ) ", REQUEST_START_AT);
            }
        }, "Volunteer");
    }

    private void showMic(String message, int requestCode) {
        micIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, message);
        startActivityForResult(micIntent, requestCode);
    }



    private class AsyncGetUser extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... param) {
            User loggedUser = userService.getLoggedUser(param[0]);
            return loggedUser;
        }

        @Override
        protected void onPostExecute(User user) {
            Location location = getMyLocation();

            if (location != null) {
                form = new VolunteerForm(user.getId(), location.getLatitude(), location.getLongitude());
            }

            showWebView(Server.WEB_VIEW_ROOT + "/volunteer/register");
            bindJavascript();
        }
    }

    private class AsyncRegister extends AsyncTask<VolunteerForm, Void, DisplayVolunteer> {
        @Override
        protected DisplayVolunteer doInBackground(VolunteerForm... param) {
            DisplayVolunteer displayVolunteer = new DisplayVolunteer();

            Volunteer volunteer = volunteerService.save(param[0]);
            displayVolunteer.setVolunteer(volunteer);
            Helper recommendHelper = volunteerService.findRecommendHelper(volunteer.getVolunteerId());
            displayVolunteer.setHelper(recommendHelper);
            return displayVolunteer;
        }

        @Override
        protected void onPostExecute(DisplayVolunteer displayVolunteer) {
            if (displayVolunteer.getHelper() != null) {
                Intent intent = new Intent(RegisterFormActivity.this, RecommendActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(RegisterFormActivity.this, RegisterDoneActivity.class);
                intent.putExtra("volunteerId", displayVolunteer.getVolunteer().getVolunteerId());
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
            GpsInfo gpsInfo = new GpsInfo(RegisterFormActivity.this);
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

        if (currentStep == REQUEST_START_AT && requestCode == REQUEST_START_AT)
            parseStartAt(sttResults);
        else if (currentStep == REQUEST_END_AT && requestCode == REQUEST_END_AT)
            parseEndAt(sttResults);
        else if (currentStep == REQUEST_MESSAGE && requestCode == REQUEST_MESSAGE) {
            form.setMessage(sttResults.get(0));
            currentStep = REQUEST_DONE;

        }
    }


    private void parseStartAt( ArrayList<String> sttResults) {
        if (IS_RETRY_TIME == false && IS_RETRY_DATE == false) {
            textParser = new TextParser(sttResults.get(0));
            parseStartAt();
        } else if (IS_RETRY_DATE) {
            textParser.setDateText(sttResults);
            parseStartAt();
        } else if (IS_RETRY_TIME) {
            textParser.setTimeText(sttResults);
            parseStartAt();
        }
    }


    private void parseStartAt() {
        try {
            form.setStartAt(textParser.parseDate());
            currentStep = REQUEST_END_AT;
            IS_RETRY_TIME = false;
            IS_RETRY_DATE = false;
        } catch (ParseDateException e) {
            IS_RETRY_DATE = true;
        } catch (ParseTimeException e) {
            IS_RETRY_TIME = true;
        }
    }



    private void parseEndAt( ArrayList<String> sttResults) {
        if (IS_RETRY_TIME == false && IS_RETRY_DATE == false) {
            textParser = new TextParser(sttResults.get(0));
            parseEndAt();
        } else if (IS_RETRY_DATE) {
            textParser.setDateText(sttResults);
            parseEndAt();
        } else if (IS_RETRY_TIME) {
            textParser.setTimeText(sttResults);
            parseEndAt();
        }
    }


    private void parseEndAt() {
        try {
            form.setEndAt(textParser.parseDate());
            currentStep = REQUEST_MESSAGE;
            IS_RETRY_TIME = false;
            IS_RETRY_DATE = false;
        } catch (ParseDateException e) {
            IS_RETRY_DATE = true;
        } catch (ParseTimeException e) {
            IS_RETRY_TIME = true;
        }
    }
}
package com.example.junmp.togetherhelpee.activity.volunteer.feedback;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.webkit.JavascriptInterface;

import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.common.AbstractWebViewActivity;
import com.example.junmp.togetherhelpee.activity.home.HomeActivity;
import com.example.junmp.togetherhelpee.activity.volunteer.VolunteerDoneActivity;
import com.example.junmp.togetherhelpee.activity.volunteer.request.form.RegisterFormActivity;
import com.example.junmp.togetherhelpee.common.constante.Server;
import com.example.junmp.togetherhelpee.common.util.text.ParseDateException;
import com.example.junmp.togetherhelpee.common.util.text.ParseIntException;
import com.example.junmp.togetherhelpee.common.util.text.ParseTimeException;
import com.example.junmp.togetherhelpee.common.util.text.TextToIntParser;
import com.example.junmp.togetherhelpee.domain.user.User;
import com.example.junmp.togetherhelpee.domain.volunteer.VolunteerForm;
import com.example.junmp.togetherhelpee.domain.volunteer.feedback.FeedbackForm;
import com.example.junmp.togetherhelpee.domain.volunteer.feedback.FeedbackService;

import java.util.ArrayList;

public class FeedbackActivity extends AbstractWebViewActivity {

    private Intent micIntent;
    private int volunteerId;
    private static final int REQUEST_MESSAGE = 1;
    private static final int REQUEST_STAR_COUNT = 2;
    private static final int REQUEST_DONE = 3;
    private static boolean IS_RETRY_STAR_COUNT = false;

    private static int currentStep = REQUEST_MESSAGE;

    private TextToIntParser textToIntParser;
    private FeedbackForm feedbackForm;
    private FeedbackService feedbackService = new FeedbackService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_request_done);
        super.initWebview(R.id.webview);
        volunteerId = getIntent().getIntExtra("volunteerId" , 0);
        micIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        micIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        micIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        feedbackForm = new FeedbackForm();
        showWebView(Server.WEB_VIEW_ROOT + "/volunteer/" +volunteerId+ "/feedback");
        bindJavascript();
    }


    private void bindJavascript() {
        webView.addJavascriptInterface(new Object() {

            @JavascriptInterface
            public void clickHome() {
                startActivity(new Intent(FeedbackActivity.this , HomeActivity.class));
                finish();
            }

            @JavascriptInterface
            public void clickMic() {
                showMic("봉사자가 어땠는지 말해 주세요. (예 : 열심히 해주셔서 고마워요 )", REQUEST_MESSAGE);
            }
        } , "Volunteer");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentStep == REQUEST_STAR_COUNT && IS_RETRY_STAR_COUNT) {
            showMic("다시 한번 이야기 해 주세요.  한개 두개 처럼 자세히 부탁드려요", REQUEST_STAR_COUNT);
        } else if (currentStep == REQUEST_DONE) {
            new AsyncSaveFeedback().execute();
        }
    }

    private class AsyncSaveFeedback extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... param) {
            feedbackService.save(feedbackForm);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent intent = new Intent(FeedbackActivity.this, VolunteerDoneActivity.class);
            intent.putExtra("volunteerId" , volunteerId);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;


        ArrayList<String> sttResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

        if (currentStep == REQUEST_MESSAGE && requestCode == REQUEST_MESSAGE) {
            feedbackForm.setMessage(sttResults.get(0));
            currentStep = REQUEST_STAR_COUNT;
        } else if (currentStep == REQUEST_STAR_COUNT && requestCode == REQUEST_STAR_COUNT) {
            parseStarCount(sttResults);
        }
    }

    private void parseStarCount(ArrayList<String> sttResults) {
        if (IS_RETRY_STAR_COUNT == false) {
            textToIntParser = new TextToIntParser(sttResults.get(0));
            parseStarCount();
        } else if (IS_RETRY_STAR_COUNT) {
            textToIntParser.setIntValue(sttResults);
            parseStarCount();
        }
    }

    private void parseStarCount() {
        try {
            feedbackForm.setStarCount(textToIntParser.parse());
            currentStep = REQUEST_DONE;
            IS_RETRY_STAR_COUNT = false;
        } catch (ParseIntException e) {
            IS_RETRY_STAR_COUNT = true;
        }
    }

    private void showMic(String message, int requestCode) {
        micIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, message);
        startActivityForResult(micIntent, requestCode);
    }
}

package com.example.junmp.togetherhelpee.activity.popup;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.junmp.togetherhelpee.R;
import com.example.junmp.togetherhelpee.activity.home.HomeActivity;
import com.example.junmp.togetherhelpee.domain.user.UserService;
import com.example.junmp.togetherhelpee.domain.volunteer.VolunteerService;

public class FcmPopupActivity extends Activity {
    private VolunteerService volunteerService = new VolunteerService();
    private UserService userService = new UserService();
    private TextView message_txt;
    private String message;
    private String volunteerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fcm_popup);

        // 키잠금 해제 및 화면 켜기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        Intent intent = getIntent();
        message = intent.getStringExtra("msg");
        volunteerId = intent.getStringExtra("volunteerId");

        Log.d("fcmpop",message);
        Log.d("fcmid", volunteerId);

        message_txt = (TextView)findViewById(R.id.fcm_txt);
        message_txt.setText(message);


    }


    //신청 버튼 클릭
    public void mOnRegister(View v){
        //데이터 전달하기
        new AsyncConfirm().execute();
    }

    private class AsyncConfirm extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            volunteerService.accept(Integer.parseInt(volunteerId));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            intent.putExtra("volunteerId" , volunteerId);
            startActivity(intent);
            finish();
        }
    }

    private class AsyncNo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            volunteerService.reject(Integer.parseInt(volunteerId));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            Intent intent = new Intent();
            intent.putExtra("result", "Close Popup");
            setResult(RESULT_OK, intent);

            //액티비티(팝업) 닫기
            finish();
        }
    }


    //확인 버튼 클릭
    public void mOnClose(View v){
        new AsyncNo().execute();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}

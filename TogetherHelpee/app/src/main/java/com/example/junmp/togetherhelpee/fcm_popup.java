package com.example.junmp.togetherhelpee;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class fcm_popup extends Activity {
    private TextView message_txt;
    private String message;
    private String id;

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
        id = intent.getStringExtra("id");

        Log.d("fcmpop",message);
        Log.d("fcmid",id);

        message_txt = (TextView)findViewById(R.id.fcm_txt);
        message_txt.setText(message);


    }


    //신청 버튼 클릭
    public void mOnRegister(View v){
        //데이터 전달하기
        Intent intent = new Intent(getApplicationContext(),Call1Activity.class);
        startActivity(intent);

    }


    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
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

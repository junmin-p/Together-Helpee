package com.example.junmp.togetherhelpee;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FeedbackActivity extends AppCompatActivity {
    TextView txt_score;
    ImageView icon1;
    ImageView icon2;
    ImageView icon3;
    ImageView icon4;
    ImageView icon5;
    ImageView btn_mic;
    Button btn_play;
    Button btn_submit;

    int volunteerId = 0;
    int helpeeScore = 5;

    static final String RECORDED_FILE = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/myrecording.mp3";
    MediaPlayer player;
    MediaRecorder recorder;
    int playbackPosition = 0;
    private int is_record = 0;
    private int is_play = 0;

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    String phone_num;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feedback);

        Intent intent = new Intent(getApplicationContext(), processTimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Intent vi = getIntent();
        volunteerId = vi.getIntExtra("volunteerId",0);
        Log.d("fdsa",String.valueOf(volunteerId));

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(FeedbackActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(FeedbackActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

        Toast.makeText(getApplicationContext(), "하트를 클릭하여 봉사자의 점수를 메겨주세요. 이후 바로 중앙의 마이크 버튼을 클릭하여 녹음을 진행해주시고 녹음을 마치시려면 다시한번 마이크버튼을, 재생하시려면 하단의 플레이버튼을, 그대로 제출하시려면 최하단의 제출버튼을 클릭해주세요.", Toast.LENGTH_LONG).show();

        txt_score = findViewById(R.id.txt_score);
        icon1 = findViewById(R.id.icon1);
        icon2 = findViewById(R.id.icon2);
        icon3 = findViewById(R.id.icon3);
        icon4 = findViewById(R.id.icon4);
        icon5 = findViewById(R.id.icon5);
        btn_mic = findViewById(R.id.img_mic);
        btn_play = findViewById(R.id.btn_play);
        btn_play.setVisibility(View.INVISIBLE);
        btn_submit = findViewById(R.id.btn_submit);


        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon1.setImageResource(R.drawable.heart);
                icon2.setImageResource(R.drawable.empty);
                icon3.setImageResource(R.drawable.empty);
                icon4.setImageResource(R.drawable.empty);
                icon5.setImageResource(R.drawable.empty);
                txt_score.setText("점수: 1점 (1점~5점)");
                Toast.makeText(getApplicationContext(), "1점을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                helpeeScore = 1;
                Toast.makeText(getApplicationContext(), "아래 마이크 버튼을 클릭하여 의견을 남겨주세요!", Toast.LENGTH_SHORT).show();
            }
        });
        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon1.setImageResource(R.drawable.heart);
                icon2.setImageResource(R.drawable.heart);
                icon3.setImageResource(R.drawable.empty);
                icon4.setImageResource(R.drawable.empty);
                icon5.setImageResource(R.drawable.empty);
                txt_score.setText("점수: 2점 (1점~5점)");
                Toast.makeText(getApplicationContext(), "2점을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                helpeeScore = 2;
                Toast.makeText(getApplicationContext(), "아래 마이크 버튼을 클릭하여 의견을 남겨주세요!", Toast.LENGTH_SHORT).show();
            }
        });
        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon1.setImageResource(R.drawable.heart);
                icon2.setImageResource(R.drawable.heart);
                icon3.setImageResource(R.drawable.heart);
                icon4.setImageResource(R.drawable.empty);
                icon5.setImageResource(R.drawable.empty);
                txt_score.setText("점수: 3점 (1점~5점)");
                Toast.makeText(getApplicationContext(), "3점을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                helpeeScore = 3;
                Toast.makeText(getApplicationContext(), "아래 마이크 버튼을 클릭하여 의견을 남겨주세요!", Toast.LENGTH_SHORT).show();
            }
        });
        icon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon1.setImageResource(R.drawable.heart);
                icon2.setImageResource(R.drawable.heart);
                icon3.setImageResource(R.drawable.heart);
                icon4.setImageResource(R.drawable.heart);
                icon5.setImageResource(R.drawable.empty);
                txt_score.setText("점수: 4점 (1점~5점)");
                Toast.makeText(getApplicationContext(), "4점을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                helpeeScore = 4;
                Toast.makeText(getApplicationContext(), "아래 마이크 버튼을 클릭하여 의견을 남겨주세요!", Toast.LENGTH_SHORT).show();
            }
        });
        icon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon1.setImageResource(R.drawable.heart);
                icon2.setImageResource(R.drawable.heart);
                icon3.setImageResource(R.drawable.heart);
                icon4.setImageResource(R.drawable.heart);
                icon5.setImageResource(R.drawable.heart);
                txt_score.setText("점수: 5점 (1점~5점)");
                Toast.makeText(getApplicationContext(), "5점을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                helpeeScore = 5;
                Toast.makeText(getApplicationContext(), "아래 마이크 버튼을 클릭하여 의견을 남겨주세요!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_record == 0){
                    if(recorder != null){
                        recorder.stop();
                        recorder.reset();
                        recorder.release();
                        recorder = null;
                    }// TODO Auto-generated method stub
                    is_record = 1;
                    btn_mic.setImageResource(R.drawable.record);
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    recorder.setOutputFile(RECORDED_FILE);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    try{
                        Toast.makeText(getApplicationContext(),
                                "녹음을 시작합니다. 한번더 누르시면 녹음을 마칩니다.", Toast.LENGTH_LONG).show();
                        btn_mic.setImageResource(R.drawable.recording);
                        recorder.prepare();
                        recorder.start();
                        btn_play.setVisibility(View.VISIBLE);
                    }catch (Exception ex){
                        Log.e("SampleAudioRecorder", "Exception : ", ex);
                        is_record = 0;
                        btn_mic.setImageResource(R.drawable.request);
                    }
                }

                else{
                    if(recorder == null)
                        return;

                    is_record = 0;
                    btn_mic.setImageResource(R.drawable.request);

                    recorder.stop();
                    recorder.reset();
                    recorder.release();
                    recorder = null;

                    Toast.makeText(getApplicationContext(),
                            "녹음이 중지되었습니다. 재생해보시려면 아래의 버튼을, 그대로 제출하시려면 최하단의 제출버튼을 클릭해주세요.", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated method stub
                }


            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_play == 0){
                    is_play = 1;
                    try{
                        playAudio(RECORDED_FILE);

                        Toast.makeText(getApplicationContext(), "녹음파일이 재생됩니다.", Toast.LENGTH_SHORT).show();
                    } catch(Exception e){
                        is_play = 0;
                        e.printStackTrace();
                    }
                }
                else{
                    if(player != null){
                        is_play = 0;
                        playbackPosition = player.getCurrentPosition();
                        player.pause();
                        Toast.makeText(getApplicationContext(), "녹음파일 재생이 중지됩니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_record == 1){
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                    is_record = 0;
                    putRecord(Uri.parse(RECORDED_FILE));
                    Intent toEnd = new Intent(FeedbackActivity.this, EndActivity.class);
                    startActivity(toEnd);
                    finish();
                }
                else{
                    putRecord(Uri.parse(RECORDED_FILE));
                    Intent toEnd = new Intent(FeedbackActivity.this, EndActivity.class);
                    startActivity(toEnd);
                    finish();
                }
            }
        });
    }

    private void playAudio(String url) throws Exception{
        killMediaPlayer();

        player = new MediaPlayer();
        player.setDataSource(url);
        player.prepare();
        player.start();
    }

    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        if(player != null){
            try {
                player.release();
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    protected void onPause(){
        if(recorder != null){
            recorder.release();
            recorder = null;
        }
        if (player != null){
            player.release();
            player = null;
        }

        super.onPause();

    }

    private void putRecord(Uri fileUri) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

// Change base URL to your upload server URL.
        RecordPutService service = new Retrofit.Builder().baseUrl("http://210.89.191.125").client(client).build().create(RecordPutService.class);


        File file = new File(String.valueOf(fileUri));
        RequestBody reqFile = RequestBody.create(MediaType.parse("file"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("recordfile", volunteerId+".mp3", reqFile);
        RequestBody volunteer_Id = RequestBody.create(MediaType.parse("text"), String.valueOf(volunteerId));
        RequestBody helpee_Score = RequestBody.create(MediaType.parse("text"), String.valueOf(helpeeScore));

        Call<ResponseBody> req = service.put(volunteer_Id, helpee_Score, body);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("fadsfsads", "Success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("fadsfsads", "afsdsdf");
            }

        });
    }
}

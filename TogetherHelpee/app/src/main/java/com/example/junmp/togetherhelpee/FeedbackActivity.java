package com.example.junmp.togetherhelpee;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    ImageView btn_play;
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

    Intent intent_rec;
    SpeechRecognizer mRecognizer;

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    String phone_num;

    ProgressDialog pd;

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent intent = new Intent(getApplicationContext(), processTimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Intent vi = getIntent();
        volunteerId = vi.getIntExtra("volunteerId",0);
        Log.d("fdsa",String.valueOf(volunteerId));

        Toast.makeText(getApplicationContext(), "하트를 클릭하여 봉사자의 점수를 메겨주세요. 이후 바로 중앙의 마이크 버튼을 클릭하여 녹음을 진행해주시고 녹음을 마치시려면 다시한번 마이크버튼을, 재생하시려면 하단의 플레이버튼을, 그대로 제출하시려면 최하단의 제출버튼을 클릭해주세요.", Toast.LENGTH_LONG).show();

        txt_score = findViewById(R.id.txt_score);
        icon1 = findViewById(R.id.icon1);
        icon2 = findViewById(R.id.icon2);
        icon3 = findViewById(R.id.icon3);
        icon4 = findViewById(R.id.icon4);
        icon5 = findViewById(R.id.icon5);
        btn_mic = findViewById(R.id.img_mic);
        btn_play = findViewById(R.id.btn_play);
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

        intent_rec = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent_rec.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent_rec.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);

        btn_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_record == 0){
                    if(recorder != null){
                        recorder.stop();
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
                        mRecognizer.startListening(intent_rec);
                        Toast.makeText(getApplicationContext(),
                                "녹음을 시작합니다. 한번더 누르시면 녹음을 마칩니다.", Toast.LENGTH_LONG).show();
                        recorder.prepare();
                        recorder.start();
                    }catch (Exception ex){
                        Log.e("SampleAudioRecorder", "Exception : ", ex);
                        is_record = 0;
                        btn_mic.setImageResource(R.drawable.mic);
                    }
                }

                else{
                    if(recorder == null)
                        return;

                    is_record = 0;
                    btn_mic.setImageResource(R.drawable.mic);

                    recorder.stop();
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
                    btn_play.setImageResource(R.drawable.pause);
                    try{
                        playAudio(RECORDED_FILE);

                        Toast.makeText(getApplicationContext(), "녹음파일이 재생됩니다.", Toast.LENGTH_SHORT).show();
                    } catch(Exception e){
                        is_play = 0;
                        btn_play.setImageResource(R.drawable.play);
                        e.printStackTrace();
                    }
                }
                else{
                    if(player != null){
                        is_play = 0;
                        btn_play.setImageResource(R.drawable.play);
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
                }
                if(message.equals(null)){
                    Toast.makeText(getApplicationContext(),"마이크를 누르시고 한 문장으로 평가해주세요.",Toast.LENGTH_SHORT).show();
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

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            pd = new ProgressDialog(FeedbackActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("요청사항을 마이크에 대고 예시와 같이 말씀해주세요.\n예시) 삼성역까지 데려다 주세요");
            pd.show();
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float v) {
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int i) {
            pd.dismiss();

            is_record = 0;
            btn_mic.setImageResource(R.drawable.mic);

            recorder.stop();
            recorder.release();
            recorder = null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onResults(Bundle bundle) {
            pd.dismiss();
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            message = rs[0];

            is_record = 0;
            btn_mic.setImageResource(R.drawable.mic);

            recorder.stop();
            recorder.release();
            recorder = null;

        }

        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };

    private void putRecord(Uri fileUri) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

// Change base URL to your upload server URL.
        RecordPutService service = new Retrofit.Builder().baseUrl("http://210.89.191.125").client(client).build().create(RecordPutService.class);


        File file = new File(String.valueOf(fileUri));
        RequestBody reqFile = RequestBody.create(MediaType.parse("file"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("recordfile", volunteerId+".mp3", reqFile);
        RequestBody helpee_Feedback = RequestBody.create(MediaType.parse("text"), message);
        RequestBody volunteer_Id = RequestBody.create(MediaType.parse("text"), String.valueOf(volunteerId));
        RequestBody helpee_Score = RequestBody.create(MediaType.parse("text"), String.valueOf(helpeeScore));

        Call<ResponseBody> req = service.put(helpee_Feedback, volunteer_Id, helpee_Score, body);
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

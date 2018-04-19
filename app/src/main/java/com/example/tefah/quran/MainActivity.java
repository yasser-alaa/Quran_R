package com.example.tefah.quran;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnSeekCompleteListener {

    private String audioPath;
    MediaRecorder recorder;
    MediaPlayer player;


@BindView(R.id.voice_recorder)
    FloatingActionButton voiceRecorder;
@BindView(R.id.test_player)
    FloatingActionButton testPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        it has a problem with accessability i can search for it later
        voiceRecorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        recorder = new MediaRecorder();
                        audioPath = Utilities.startRecording(recorder, MainActivity.this);
                        break;
                    case MotionEvent.ACTION_UP:
                        Utilities.stopRecording(recorder);
                        break;
                    default:
                }
                return true;
            }
        });
    }

    @OnClick(R.id.test_player)
    public void play(){
        if (player != null){
            onSeekComplete(player);
        }
        player = new MediaPlayer();
        if (audioPath != null)
            Utilities.startPlaying(player, audioPath, this);
        else
            Toast.makeText(this, getString(R.string.no_audio_recorded), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        Utilities.stopPlaying(mediaPlayer);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null){
            onSeekComplete(player);
        }
    }
}

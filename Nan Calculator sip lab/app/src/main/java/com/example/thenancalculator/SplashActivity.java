package com.example.thenancalculator;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import com.google.android.material.button.MaterialButton;

public class SplashActivity extends AppCompatActivity {

    public static MediaPlayer globalBgPlayer;
    public static boolean isSoundEnabled = true;
    public static SoundPool soundPool;
    public static int clickSoundId, refillSoundId, mixSoundId, readySoundId;

    private boolean isTransitioned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initAudio();

        VideoView videoView = findViewById(R.id.videoBackground);
        View logo = findViewById(R.id.splash_logo);
        View title = findViewById(R.id.splash_title);
        MaterialButton btnStart = findViewById(R.id.btn_start);

        try {
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro_video);
            videoView.setVideoURI(videoUri);
            videoView.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                videoView.start();
            });
        } catch (Exception e) {
            if (videoView != null) {
                videoView.setVisibility(View.GONE);
            }
        }

        // Click sound for everything in splash
        if (logo != null) {
            logo.setOnClickListener(v -> playSfx(clickSoundId));
            logo.animate().alpha(1f).scaleX(1f).scaleY(1f).rotation(360f).setDuration(1500).setInterpolator(new AnticipateOvershootInterpolator()).start();
        }
        if (title != null) {
            title.setOnClickListener(v -> playSfx(clickSoundId));
            title.animate().alpha(1f).translationY(0).setDuration(1200).setStartDelay(800).setInterpolator(new OvershootInterpolator(1.2f)).start();
        }
        if (btnStart != null) {
            btnStart.animate().alpha(1f).translationY(0).setDuration(1000).setStartDelay(1200).setInterpolator(new OvershootInterpolator()).start();
            btnStart.setOnClickListener(v -> {
                playSfx(clickSoundId);
                proceedToMain();
            });
        }
    }

    private void initAudio() {
        if (globalBgPlayer == null) {
            try {
                globalBgPlayer = MediaPlayer.create(this, R.raw.bgsound);
                if (globalBgPlayer != null) {
                    globalBgPlayer.setLooping(true);
                    if (isSoundEnabled) {
                        globalBgPlayer.start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (soundPool == null) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(audioAttributes)
                    .build();

            clickSoundId = soundPool.load(this, R.raw.buttonclick, 1);
            refillSoundId = soundPool.load(this, R.raw.glassrefill, 1);
            mixSoundId = soundPool.load(this, R.raw.mixmachine, 1);
            readySoundId = soundPool.load(this, R.raw.drinksready, 1);
        }
    }

    public static void playSfx(int soundId) {
        if (isSoundEnabled && soundPool != null) {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f);
        }
    }

    private void proceedToMain() {
        if (isTransitioned) return;
        isTransitioned = true;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

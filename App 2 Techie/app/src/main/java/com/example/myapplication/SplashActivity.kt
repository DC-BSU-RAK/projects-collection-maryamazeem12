package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logo)
        
        // Dynamic entry animation for logo
        val rotatePopIn = AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in)
        logo.startAnimation(rotatePopIn)
        
        // Loop a float animation after entrance
        logo.postDelayed({
            logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_anim))
        }, 800)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, OnboardingActivity::class.java))
            overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
            finish()
        }, 3000)
    }
}
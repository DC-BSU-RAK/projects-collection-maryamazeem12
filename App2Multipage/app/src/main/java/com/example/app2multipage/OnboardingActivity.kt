package com.example.app2multipage

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val btnForward = findViewById<ImageView>(R.id.btn_forward)
        val shoppingText = findViewById<ImageView>(R.id.shopping_text)

        // Entry animations
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val slideIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        
        shoppingText.startAnimation(slideIn)
        btnForward.startAnimation(fadeIn)

        btnForward.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
            btnForward.startAnimation(animation)

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}
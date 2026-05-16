package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val btnProceed = findViewById<ImageView>(R.id.btn_proceed)
        val infoContainer = findViewById<ConstraintLayout>(R.id.info_container)
        val heading = findViewById<ImageView>(R.id.about_heading)
        val logoSmall = findViewById<ImageView>(R.id.logo_small)

        // More Visible Entry animations
        val rotatePopIn = AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in)
        val slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right_overshoot)
        val slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left_overshoot)
        val floatAnim = AnimationUtils.loadAnimation(this, R.anim.float_anim)

        heading.startAnimation(rotatePopIn)
        infoContainer.startAnimation(slideInRight)
        logoSmall?.startAnimation(slideInLeft)
        btnProceed.startAnimation(rotatePopIn)
        
        // Add floating effect after entry
        heading.postDelayed({ heading.startAnimation(floatAnim) }, 1000)

        btnProceed.setOnClickListener {
            SoundHelper.playClickSound(this)
            val clickAnim = AnimationUtils.loadAnimation(this, R.anim.button_click)
            btnProceed.startAnimation(clickAnim)

            it.postDelayed({
                val intent = Intent(this, CategoryActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
            }, 200)
        }
        
        logoSmall?.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            finish()
        }
    }
}
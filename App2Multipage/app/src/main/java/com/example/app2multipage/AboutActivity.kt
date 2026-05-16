package com.example.app2multipage

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

        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        heading.startAnimation(fadeIn)
        infoContainer.startAnimation(slideUp)

        btnProceed.setOnClickListener {
            SoundHelper.playClickSound(this)
            val clickAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
            btnProceed.startAnimation(clickAnim)

            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}
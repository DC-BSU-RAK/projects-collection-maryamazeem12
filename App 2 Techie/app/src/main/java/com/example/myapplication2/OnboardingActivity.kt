package com.example.myapplication2

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
        val backgroundImage = findViewById<ImageView>(R.id.background_image)

        // Visible Entry animations
        val slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right_overshoot)
        val rotatePopIn = AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in)
        val floatAnim = AnimationUtils.loadAnimation(this, R.anim.float_anim)
        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)

        backgroundImage.startAnimation(zoomIn)
        shoppingText.startAnimation(slideInRight)
        btnForward.startAnimation(rotatePopIn)
        
        // Add shaky animation to the main text after entry
        shoppingText.postDelayed({
            shoppingText.startAnimation(shake)
        }, 1200)

        // Floating effect for the button
        btnForward.postDelayed({
            btnForward.startAnimation(floatAnim)
        }, 1000)

        btnForward.setOnClickListener {
            SoundHelper.playClickSound(this)
            val clickAnim = AnimationUtils.loadAnimation(this, R.anim.button_click)
            btnForward.startAnimation(clickAnim)

            it.postDelayed({
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
            }, 200)
        }
    }
}
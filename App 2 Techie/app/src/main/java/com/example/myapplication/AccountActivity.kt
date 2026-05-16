package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView

class AccountActivity : AppCompatActivity() {

    private lateinit var ivProfileLarge: ShapeableImageView
    private lateinit var tvNameLarge: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        ivProfileLarge = findViewById(R.id.iv_profile_large)
        tvNameLarge = findViewById(R.id.tv_name_large)
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnLogout = findViewById<View>(R.id.btn_logout)
        val btnMyOrders = findViewById<View>(R.id.btn_my_orders)
        val btnEditProfile = findViewById<View>(R.id.btn_edit_profile)
        val optionsContainer = findViewById<View>(R.id.options_container)
        val tvTitle = findViewById<View>(R.id.tv_title)

        // Highly Visible Advanced Entry animations
        val rotatePopIn = AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in)
        val slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right_overshoot)
        val slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left_overshoot)
        val smoothScale = AnimationUtils.loadAnimation(this, R.anim.smooth_scale_in)
        val overshootSlide = AnimationUtils.loadAnimation(this, R.anim.overshoot_slide_in)

        tvTitle.startAnimation(rotatePopIn)
        ivProfileLarge.startAnimation(smoothScale)
        tvNameLarge.startAnimation(slideInRight)
        optionsContainer.startAnimation(overshootSlide)
        btnBack.startAnimation(slideInLeft)

        refreshUserData()

        btnBack.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            it.postDelayed({ finish() }, 200)
        }

        btnMyOrders.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
        }

        btnEditProfile.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            it.postDelayed({
                startActivity(Intent(this, EditProfileActivity::class.java))
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
            }, 200)
        }

        btnLogout.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            it.postDelayed({
                val intent = Intent(this, OnboardingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
                finish()
            }, 200)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUserData()
    }

    private fun refreshUserData() {
        val sharedPref = getSharedPreferences("TechiePrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", "User")
        val profilePicResId = sharedPref.getInt("profile_pic", R.drawable.profilepic)

        tvNameLarge.text = userName
        ivProfileLarge.setImageResource(profilePicResId)
    }
}
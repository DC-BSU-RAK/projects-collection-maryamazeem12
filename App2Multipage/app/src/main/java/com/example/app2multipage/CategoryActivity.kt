package com.example.app2multipage

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView

class CategoryActivity : AppCompatActivity() {

    private lateinit var ivUserProfile: ShapeableImageView
    private lateinit var tvWelcome: TextView
    private lateinit var navProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        ivUserProfile = findViewById(R.id.iv_user_profile)
        tvWelcome = findViewById(R.id.tv_welcome)
        val btnSettings = findViewById<ImageView>(R.id.btn_settings)
        navProfile = findViewById(R.id.nav_profile)

        refreshUserData()

        btnSettings.setOnClickListener {
            SoundHelper.playClickSound(this)
            showSettingsDialog()
        }

        ivUserProfile.setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, AccountActivity::class.java))
        }

        findViewById<ImageView>(R.id.logo_small).setOnClickListener {
            SoundHelper.playClickSound(this)
            // Already home
        }

        // Setup each Category Item
        setupCategory(R.id.cat_airpods, "Airpods", R.drawable.airpodcatgeory) {
            startActivity(Intent(this, AirpodsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        
        setupCategory(R.id.cat_headset, "Headset", R.drawable.headsetcategory) {
            startActivity(Intent(this, HeadsetActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        
        setupCategory(R.id.cat_speaker, "Speaker", R.drawable.speakerscategory) {
            startActivity(Intent(this, SpeakerActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        
        setupCategory(R.id.cat_smartwatch, "Smart Watch", R.drawable.smartwatccategory) {
            startActivity(Intent(this, SmartwatchActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        
        setupCategory(R.id.cat_macbook, "MacBook", R.drawable.macbookcategory) {
            startActivity(Intent(this, MacbookActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        
        setupCategory(R.id.cat_polaroids, "Polaroids", R.drawable.polaroidscategory) {
            startActivity(Intent(this, PolaroidsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Bottom Navigation
        findViewById<View>(R.id.nav_home).setOnClickListener {
            SoundHelper.playClickSound(this)
        }
        findViewById<View>(R.id.nav_fav).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, FavoritesActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        findViewById<View>(R.id.nav_cart).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, CartActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        findViewById<View>(R.id.nav_profile).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, AccountActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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

        tvWelcome.text = "Welcome! $userName"
        ivUserProfile.setImageResource(profilePicResId)
        navProfile.setImageResource(profilePicResId)
    }

    private fun setupCategory(includeId: Int, name: String, imageRes: Int, onClick: () -> Unit) {
        val categoryView = findViewById<View>(includeId)
        val imageView = categoryView.findViewById<ImageView>(R.id.iv_cat_img)
        val textView = categoryView.findViewById<TextView>(R.id.tv_cat_name)

        imageView.setImageResource(imageRes)
        textView.text = name

        categoryView.setOnClickListener {
            SoundHelper.playClickSound(this)
            categoryView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))
            onClick()
        }
    }

    private fun showSettingsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_settings)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setWindowAnimations(android.R.style.Animation_Dialog)
        dialog.findViewById<TextView>(R.id.btn_close_settings).setOnClickListener {
            SoundHelper.playClickSound(this)
            dialog.dismiss()
        }
        dialog.show()
    }
}
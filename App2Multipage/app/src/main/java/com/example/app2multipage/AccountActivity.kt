package com.example.app2multipage

import android.content.Intent
import android.os.Bundle
import android.view.View
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

        refreshUserData()

        btnBack.setOnClickListener {
            SoundHelper.playClickSound(this)
            finish()
        }

        btnMyOrders.setOnClickListener {
            SoundHelper.playClickSound(this)
        }

        btnEditProfile.setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        btnLogout.setOnClickListener {
            SoundHelper.playClickSound(this)
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
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
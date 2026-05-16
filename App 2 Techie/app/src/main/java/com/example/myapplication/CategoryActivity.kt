package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.GridLayout
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
        val tvSelectCategory = findViewById<TextView>(R.id.tv_select_category)
        val categoryGrid = findViewById<GridLayout>(R.id.category_grid)

        // Highly Visible Advanced Entry animations
        val rotatePopIn = AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in)
        val slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left_overshoot)
        val slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right_overshoot)
        val staggeredEntrance = AnimationUtils.loadLayoutAnimation(this, R.anim.staggered_entrance)
        
        ivUserProfile.startAnimation(rotatePopIn)
        tvWelcome.startAnimation(slideInRight)
        tvSelectCategory.startAnimation(slideInLeft)
        btnSettings.startAnimation(rotatePopIn)
        
        // Staggered layout animation for categories
        categoryGrid.layoutAnimation = staggeredEntrance

        refreshUserData()

        btnSettings.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            showSettingsDialog()
        }

        ivUserProfile.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            it.postDelayed({
                startActivity(Intent(this, EditProfileActivity::class.java))
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
            }, 200)
        }

        setupCategory(R.id.cat_airpods, "Airpods", R.drawable.airpodcatgeory) {
            startActivity(Intent(this, AirpodsActivity::class.java))
        }
        
        setupCategory(R.id.cat_headset, "Headset", R.drawable.headsetcategory) {
            startActivity(Intent(this, HeadsetActivity::class.java))
        }
        
        setupCategory(R.id.cat_speaker, "Speaker", R.drawable.speakerscategory) {
            startActivity(Intent(this, SpeakerActivity::class.java))
        }
        
        setupCategory(R.id.cat_smartwatch, "Smart Watch", R.drawable.smartwatccategory) {
            startActivity(Intent(this, SmartwatchActivity::class.java))
        }
        
        setupCategory(R.id.cat_macbook, "MacBook", R.drawable.macbookcategory) {
            startActivity(Intent(this, MacbookActivity::class.java))
        }
        
        setupCategory(R.id.cat_polaroids, "Polaroids", R.drawable.polaroidscategory) {
            startActivity(Intent(this, PolaroidsActivity::class.java))
        }

        val navItems = listOf(R.id.nav_home, R.id.nav_fav, R.id.nav_cart, R.id.nav_profile)
        navItems.forEach { id ->
            findViewById<View>(id)?.setOnClickListener {
                SoundHelper.playClickSound(this)
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
                it.postDelayed({
                    when(id) {
                        R.id.nav_fav -> startActivity(Intent(this, FavoritesActivity::class.java))
                        R.id.nav_cart -> startActivity(Intent(this, CartActivity::class.java))
                        R.id.nav_profile -> startActivity(Intent(this, EditProfileActivity::class.java))
                    }
                    if (id != R.id.nav_home) {
                        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
                    }
                }, 200)
            }
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
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            it.postDelayed({ 
                onClick()
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
            }, 200)
        }
    }

    private fun showSettingsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_settings)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        val dialogView = dialog.window?.decorView
        dialogView?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in))

        dialog.findViewById<TextView>(R.id.btn_close_settings)?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
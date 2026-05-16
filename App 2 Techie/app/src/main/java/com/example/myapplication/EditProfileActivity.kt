package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.imageview.ShapeableImageView

class EditProfileActivity : AppCompatActivity() {

    private lateinit var ivProfilePic: ShapeableImageView
    private lateinit var navProfile: ShapeableImageView
    private var selectedAvatarResId: Int = R.drawable.profilepic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        ivProfilePic = findViewById(R.id.iv_profile_pic)
        navProfile = findViewById(R.id.nav_profile)
        val btnChangePic = findViewById<ImageView>(R.id.btn_change_pic)
        val btnSave = findViewById<ImageView>(R.id.btn_save_changes)
        val btnLogout = findViewById<ImageView>(R.id.btn_logout)
        val etFullName = findViewById<EditText>(R.id.et_fullname)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val logoSmall = findViewById<ImageView>(R.id.logo_small)
        val btnSettings = findViewById<ImageView>(R.id.btn_settings)
        val formContainer = findViewById<ConstraintLayout>(R.id.form_container)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val profileContainer = findViewById<View>(R.id.profile_container)

        // Load current data
        val sharedPref = getSharedPreferences("TechiePrefs", MODE_PRIVATE)
        etFullName.setText(sharedPref.getString("user_name", ""))
        etEmail.setText(sharedPref.getString("user_email", ""))
        etUsername.setText(sharedPref.getString("user_username", ""))
        etPassword.setText(sharedPref.getString("user_password", ""))
        selectedAvatarResId = sharedPref.getInt("profile_pic", R.drawable.profilepic)
        ivProfilePic.setImageResource(selectedAvatarResId)
        navProfile.setImageResource(selectedAvatarResId)

        // Advanced Entry animations
        val entrance = AnimationUtils.loadAnimation(this, R.anim.entrance_anim)
        val smoothScale = AnimationUtils.loadAnimation(this, R.anim.smooth_scale_in)
        val overshootSlide = AnimationUtils.loadAnimation(this, R.anim.overshoot_slide_in)

        tvTitle.startAnimation(entrance)
        profileContainer?.startAnimation(smoothScale)
        formContainer.startAnimation(overshootSlide)

        logoSmall.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            it.postDelayed({
                startActivity(Intent(this, CategoryActivity::class.java))
                finish()
            }, 200)
        }

        btnSettings.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            showSettingsDialog()
        }

        btnChangePic.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            showProfileSelectionDialog()
        }

        btnSave.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            
            val name = etFullName.text.toString()
            val email = etEmail.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            with(sharedPref.edit()) {
                putString("user_name", name)
                putString("user_email", email)
                putString("user_username", username)
                putString("user_password", password)
                putInt("profile_pic", selectedAvatarResId)
                apply()
            }

            it.postDelayed({
                Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show()
            }, 200)
        }

        btnLogout.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            it.postDelayed({
                val intent = Intent(this, OnboardingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }, 200)
        }

        // Bottom Navigation
        val navIds = listOf(R.id.nav_home, R.id.nav_fav, R.id.nav_cart)
        navIds.forEach { id ->
            findViewById<View>(id)?.setOnClickListener {
                SoundHelper.playClickSound(this)
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
                it.postDelayed({
                    val intent = when(id) {
                        R.id.nav_home -> Intent(this, CategoryActivity::class.java)
                        R.id.nav_fav -> Intent(this, FavoritesActivity::class.java)
                        else -> Intent(this, CartActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                }, 200)
            }
        }
    }

    private fun showProfileSelectionDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_profile_selection)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        val dialogView = dialog.window?.decorView
        dialogView?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.entrance_anim))

        val avatarIds = listOf(
            R.id.avatar1, R.id.avatar2, R.id.avatar3, R.id.avatar4,
            R.id.avatar5, R.id.avatar6, R.id.avatar7, R.id.avatar8
        )

        val drawableIds = arrayOf(
            R.drawable.person1, R.drawable.person2, R.drawable.person3, R.drawable.person4,
            R.drawable.person5, R.drawable.person6, R.drawable.person7, R.drawable.person8
        )

        avatarIds.forEachIndexed { index, id ->
            dialog.findViewById<ImageView>(id)?.setOnClickListener {
                SoundHelper.playClickSound(this)
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
                selectedAvatarResId = drawableIds[index]
                ivProfilePic.setImageResource(selectedAvatarResId)
                navProfile.setImageResource(selectedAvatarResId)
                it.postDelayed({ dialog.dismiss() }, 200)
            }
        }
        dialog.show()
    }

    private fun showSettingsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_settings)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        val dialogView = dialog.window?.decorView
        dialogView?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.entrance_anim))

        dialog.findViewById<TextView>(R.id.btn_close_settings)?.setOnClickListener {
            SoundHelper.playClickSound(this)
            dialog.dismiss()
        }
        dialog.show()
    }
}
package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class SignUpActivity : AppCompatActivity() {

    private lateinit var ivProfilePic: ImageView
    private var selectedAvatarResId: Int = R.drawable.profilepic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        ivProfilePic = findViewById(R.id.iv_profile_pic)
        val btnAddPic = findViewById<ImageView>(R.id.btn_add_pic)
        val btnProceed = findViewById<ImageView>(R.id.btn_proceed)
        val etFullName = findViewById<EditText>(R.id.et_fullname)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val formContainer = findViewById<ConstraintLayout>(R.id.form_container)
        val profileContainer = findViewById<FrameLayout>(R.id.profile_container)
        val logoSmall = findViewById<ImageView>(R.id.logo_small)
        val signupHeader = findViewById<ImageView>(R.id.signup_header)
        val selectText = findViewById<ImageView>(R.id.select_text)
        
        // Highly Visible Advanced Entry animations
        val rotatePopIn = AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in)
        val slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left_overshoot)
        val slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right_overshoot)
        val overshootSlide = AnimationUtils.loadAnimation(this, R.anim.overshoot_slide_in)
        val smoothScale = AnimationUtils.loadAnimation(this, R.anim.smooth_scale_in)

        signupHeader.startAnimation(rotatePopIn)
        profileContainer.startAnimation(smoothScale)
        selectText.startAnimation(slideInLeft)
        formContainer.startAnimation(overshootSlide)
        logoSmall.startAnimation(rotatePopIn)

        btnAddPic.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            showProfileSelectionDialog()
        }

        btnProceed.setOnClickListener {
            SoundHelper.playClickSound(this)
            val clickAnim = AnimationUtils.loadAnimation(this, R.anim.button_click)
            btnProceed.startAnimation(clickAnim)
            
            val fullName = etFullName.text.toString()
            val email = etEmail.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                showWarningDialog("Please fill the form to proceed!")
                return@setOnClickListener
            }
            
            val sharedPref = getSharedPreferences("TechiePrefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("user_name", fullName)
                putString("user_email", email)
                putString("user_username", username)
                putString("user_password", password)
                putInt("profile_pic", selectedAvatarResId)
                apply()
            }

            it.postDelayed({
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
            }, 200)
        }

        logoSmall.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            finish()
        }
    }

    private fun showWarningDialog(message: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_warning)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tvMessage = dialog.findViewById<TextView>(R.id.tv_warning_message)
        tvMessage?.text = message
        
        val dialogView = dialog.window?.decorView
        dialogView?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in))
        
        dialog.show()
        tvMessage?.postDelayed({ dialog.dismiss() }, 2000)
    }

    private fun showProfileSelectionDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_profile_selection)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        val dialogView = dialog.window?.decorView
        dialogView?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in))

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
                it.postDelayed({ dialog.dismiss() }, 200)
            }
        }

        dialog.show()
    }
}
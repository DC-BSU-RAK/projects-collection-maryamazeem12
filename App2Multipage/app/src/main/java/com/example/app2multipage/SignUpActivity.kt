package com.example.app2multipage

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
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

        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        findViewById<ImageView>(R.id.signup_header).startAnimation(fadeIn)
        profileContainer.startAnimation(fadeIn)
        formContainer.startAnimation(slideUp)

        btnAddPic.setOnClickListener {
            SoundHelper.playClickSound(this)
            showProfileSelectionDialog()
        }

        btnProceed.setOnClickListener {
            SoundHelper.playClickSound(this)
            val fullName = etFullName.text.toString().ifEmpty { "User" }
            val email = etEmail.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            
            val sharedPref = getSharedPreferences("TechiePrefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("user_name", fullName)
                putString("user_email", email)
                putString("user_username", username)
                putString("user_password", password)
                putInt("profile_pic", selectedAvatarResId)
                apply()
            }

            it.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))

            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        logoSmall.setOnClickListener {
            SoundHelper.playClickSound(this)
            finish()
        }

        val fields = listOf(etFullName, etEmail, etUsername, etPassword)
        fields.forEach { field ->
            field.setOnClickListener { SoundHelper.playClickSound(this) }
            field.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) SoundHelper.playClickSound(this)
            }
        }
    }

    private fun showProfileSelectionDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_profile_selection)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setWindowAnimations(android.R.style.Animation_Dialog)

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
                selectedAvatarResId = drawableIds[index]
                ivProfilePic.setImageResource(selectedAvatarResId)
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}
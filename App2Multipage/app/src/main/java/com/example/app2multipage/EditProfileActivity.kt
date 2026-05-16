package com.example.app2multipage

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class EditProfileActivity : AppCompatActivity() {

    private lateinit var ivProfilePic: ImageView
    private var selectedAvatarResId: Int = R.drawable.profilepic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        ivProfilePic = findViewById(R.id.iv_profile_pic)
        val btnChangePic = findViewById<ImageView>(R.id.btn_change_pic)
        val btnSave = findViewById<TextView>(R.id.btn_save_changes)
        val etFullName = findViewById<EditText>(R.id.et_fullname)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val logoSmall = findViewById<ImageView>(R.id.logo_small)
        val btnSettings = findViewById<ImageView>(R.id.btn_settings)
        val formContainer = findViewById<ConstraintLayout>(R.id.form_container)

        // Load current data
        val sharedPref = getSharedPreferences("TechiePrefs", MODE_PRIVATE)
        etFullName.setText(sharedPref.getString("user_name", ""))
        etEmail.setText(sharedPref.getString("user_email", ""))
        etUsername.setText(sharedPref.getString("user_username", ""))
        etPassword.setText(sharedPref.getString("user_password", ""))
        selectedAvatarResId = sharedPref.getInt("profile_pic", R.drawable.profilepic)
        ivProfilePic.setImageResource(selectedAvatarResId)

        // Animations like Sign Up
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        findViewById<TextView>(R.id.tv_title).startAnimation(fadeIn)
        formContainer.startAnimation(slideUp)

        logoSmall.setOnClickListener {
            SoundHelper.playClickSound(this)
            finish()
        }

        btnSettings.setOnClickListener {
            SoundHelper.playClickSound(this)
            showSettingsDialog()
        }

        btnChangePic.setOnClickListener {
            SoundHelper.playClickSound(this)
            showProfileSelectionDialog()
        }

        btnSave.setOnClickListener {
            SoundHelper.playClickSound(this)
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

            Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Add sound to edit texts
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

    private fun showSettingsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_settings)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<TextView>(R.id.btn_close_settings)?.setOnClickListener {
            SoundHelper.playClickSound(this)
            dialog.dismiss()
        }
        dialog.show()
    }
}
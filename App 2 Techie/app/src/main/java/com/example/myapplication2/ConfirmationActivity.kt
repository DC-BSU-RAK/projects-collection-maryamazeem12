package com.example.myapplication2

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class ConfirmationActivity : AppCompatActivity() {

    private var successMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        // Play success sound once on launch
        playSuccessSound()

        // Clear cart when order is confirmed
        CartManager.clearCart()

        val videoView = findViewById<VideoView>(R.id.vv_success)
        val btnContinue = findViewById<ImageView>(R.id.btn_continue_shopping)
        val tvTitle = findViewById<TextView>(R.id.tv_confirm_title)
        val ivProgress = findViewById<ImageView>(R.id.iv_progress_confirm)
        val logoSmall = findViewById<ImageView>(R.id.logo_small)
        val btnSettings = findViewById<ImageView>(R.id.btn_settings)

        // Advanced Entry animations
        val entrance = AnimationUtils.loadAnimation(this, R.anim.entrance_anim)
        val smoothScale = AnimationUtils.loadAnimation(this, R.anim.smooth_scale_in)
        val overshootSlide = AnimationUtils.loadAnimation(this, R.anim.overshoot_slide_in)
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        val rotatePopIn = AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in)
        
        tvTitle?.startAnimation(entrance)
        ivProgress?.startAnimation(entrance)
        videoView.startAnimation(smoothScale)
        btnContinue.startAnimation(rotatePopIn)
        logoSmall?.startAnimation(rotatePopIn)
        btnSettings?.startAnimation(rotatePopIn)
        
        // Add shaky heading after entrance
        tvTitle?.postDelayed({ tvTitle.startAnimation(shake) }, 1200)

        // Setup and play confirmordervideo in loop
        val videoPath = "android.resource://" + packageName + "/" + R.raw.confirmordervideo
        videoView.setVideoURI(Uri.parse(videoPath))
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
            videoView.start()
        }

        btnContinue.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            
            it.postDelayed({
                // Navigate back to CategoryActivity and clear activity stack
                val intent = Intent(this, CategoryActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
                finish()
            }, 200)
        }

        btnSettings.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            showSettingsDialog()
        }
        
        logoSmall.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            finish()
        }
    }

    private fun playSuccessSound() {
        successMediaPlayer = MediaPlayer.create(this, R.raw.successsound)
        successMediaPlayer?.start()
        successMediaPlayer?.setOnCompletionListener {
            it.release()
            successMediaPlayer = null
        }
    }

    private fun showSettingsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_settings)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        val dialogView = dialog.window?.decorView
        dialogView?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in))

        dialog.findViewById<TextView>(R.id.btn_close_settings)?.setOnClickListener {
            SoundHelper.playClickSound(this)
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        successMediaPlayer?.release()
    }
}
package com.example.app2multipage

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
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

        // Setup and play confirmordervideo in loop
        val videoPath = "android.resource://" + packageName + "/" + R.raw.confirmordervideo
        videoView.setVideoURI(Uri.parse(videoPath))
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
            videoView.start()
        }

        btnContinue.setOnClickListener {
            SoundHelper.playClickSound(this)
            // Navigate back to CategoryActivity and clear activity stack
            val intent = Intent(this, CategoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        findViewById<View>(R.id.btn_settings).setOnClickListener {
            SoundHelper.playClickSound(this)
            showSettingsDialog()
        }
        
        findViewById<View>(R.id.logo_small).setOnClickListener {
            SoundHelper.playClickSound(this)
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
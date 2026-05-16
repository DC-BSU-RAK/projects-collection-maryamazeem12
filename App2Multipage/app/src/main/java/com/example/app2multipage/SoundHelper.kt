package com.example.app2multipage

import android.content.Context
import android.media.MediaPlayer

object SoundHelper {
    private var mediaPlayer: MediaPlayer? = null

    fun playClickSound(context: Context) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, R.raw.btnclick)
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener { it.release() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
package com.example.myapplication

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler

object MusicPlayerManager {
    var mediaPlayer: MediaPlayer? = null
    var isPlaying = false

    private val handler = Handler()
    private val progressListeners = mutableListOf<(Int) -> Unit>()
    private val stateListeners = mutableListOf<(Boolean) -> Unit>()

    fun initialize(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.bluedream_cheel)
        }
    }

    fun play() {
        mediaPlayer?.start()
        isPlaying = true
        notifyStateChange()
        startProgressUpdates()
    }

    fun pause() {
        mediaPlayer?.pause()
        isPlaying = false
        notifyStateChange()
    }

    fun seekTo(pos: Int) {
        mediaPlayer?.seekTo(pos)
    }

    fun getPosition(): Int = mediaPlayer?.currentPosition ?: 0
    fun getDuration(): Int = mediaPlayer?.duration ?: 1000

    fun addOnProgressUpdateListener(listener: (Int) -> Unit) {
        if (!progressListeners.contains(listener)) progressListeners.add(listener)
    }

    fun removeOnProgressUpdateListener(listener: (Int) -> Unit) {
        progressListeners.remove(listener)
    }

    fun addOnStateChangeListener(listener: (Boolean) -> Unit) {
        if (!stateListeners.contains(listener)) stateListeners.add(listener)
    }

    fun removeOnStateChangeListener(listener: (Boolean) -> Unit) {
        stateListeners.remove(listener)
    }

    private fun notifyStateChange() {
        stateListeners.forEach { it(isPlaying) }
    }

    private val updateRunnable = object : Runnable {
        override fun run() {
            val position = getPosition()
            progressListeners.forEach { it(position) }
            handler.postDelayed(this, 500)
        }
    }

    fun startProgressUpdates() {
        handler.post(updateRunnable)
    }

    fun stopProgressUpdates() {
        handler.removeCallbacks(updateRunnable)
    }

    fun release() {
        stopProgressUpdates()
        mediaPlayer?.release()
        mediaPlayer = null
        progressListeners.clear()
        stateListeners.clear()
    }
}

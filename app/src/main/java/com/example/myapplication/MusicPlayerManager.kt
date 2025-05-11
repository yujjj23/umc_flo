package com.example.myapplication

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.util.Log

//object MusicPlayerManager {
//    var mediaPlayer: MediaPlayer? = null
//    var isPlaying = false
//
//    private val handler = Handler()
//    private val progressListeners = mutableListOf<(Int) -> Unit>()
//    private val stateListeners = mutableListOf<(Boolean) -> Unit>()
//
//    fun initialize(context: Context) {
//        if (mediaPlayer == null) {
//            mediaPlayer = MediaPlayer.create(context, R.raw.bluedream_cheel)
//        }
//    }
////    fun playNewSong(context: Context, resId: Int) {
////        release() // 기존 플레이어 해제
////        mediaPlayer = MediaPlayer.create(context, resId)
////        mediaPlayer?.start()
////        isPlaying = true
////        notifyStateChange()
////        startProgressUpdates()
////    }
//fun playNewSong(context: Context, resId: Int) {
//    release() // 기존 플레이어 해제
//    mediaPlayer = MediaPlayer.create(context, resId)
//
//    // 곡 재생 시작
//    mediaPlayer?.start()
//}
//
//    fun setOnCompletionListener(listener: () -> Unit) {
//        onCompletionListener = listener
//        mediaPlayer?.setOnCompletionListener {
//            onCompletionListener?.invoke()  // 곡이 끝날 때 listener 호출
//        }
//    }
//
//
//    fun play() {
//        mediaPlayer?.start()
//        isPlaying = true
//        notifyStateChange()
//        startProgressUpdates()
//    }
//
//    fun pause() {
//        mediaPlayer?.pause()
//        isPlaying = false
//        notifyStateChange()
//    }
//
//    fun seekTo(pos: Int) {
//        mediaPlayer?.seekTo(pos)
//    }
//
//    fun getPosition(): Int = mediaPlayer?.currentPosition ?: 0
//    fun getDuration(): Int = mediaPlayer?.duration ?: 1000
//
//    fun addOnProgressUpdateListener(listener: (Int) -> Unit) {
//        if (!progressListeners.contains(listener)) progressListeners.add(listener)
//    }
//
//    fun removeOnProgressUpdateListener(listener: (Int) -> Unit) {
//        progressListeners.remove(listener)
//    }
//
//    fun addOnStateChangeListener(listener: (Boolean) -> Unit) {
//        if (!stateListeners.contains(listener)) stateListeners.add(listener)
//    }
//
//    fun removeOnStateChangeListener(listener: (Boolean) -> Unit) {
//        stateListeners.remove(listener)
//    }
//
//    private fun notifyStateChange() {
//        stateListeners.forEach { it(isPlaying) }
//    }
//
//    private val updateRunnable = object : Runnable {
//        override fun run() {
//            val position = getPosition()
//            progressListeners.forEach { it(position) }
//            handler.postDelayed(this, 500)
//        }
//    }
//
//    fun startProgressUpdates() {
//        handler.post(updateRunnable)
//    }
//
//    fun stopProgressUpdates() {
//        handler.removeCallbacks(updateRunnable)
//    }
//
//    fun release() {
//        stopProgressUpdates()
//        mediaPlayer?.release()
//        mediaPlayer = null
//        progressListeners.clear()
//        stateListeners.clear()
//    }
//    fun setMusic(context: Context, resId: Int) {
//        release()
//        if (resId == 0) {
//            Log.e("MusicPlayerManager", "Invalid resId passed to setMusic()")
//            return
//        }
//        mediaPlayer = MediaPlayer.create(context, resId)
//    }
//
//
//}


object MusicPlayerManager {
    var mediaPlayer: MediaPlayer? = null
    var isPlaying = false

    private val handler = Handler()
    private val progressListeners = mutableListOf<(Int) -> Unit>()
    private val stateListeners = mutableListOf<(Boolean) -> Unit>()

    // onCompletionListener를 설정할 수 있는 변수
    private var onCompletionListener: (() -> Unit)? = null

    fun initialize(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.bluedream_cheel)
        }
    }

    fun setOnCompletionListener(listener: () -> Unit) {
        onCompletionListener = listener
        mediaPlayer?.setOnCompletionListener {
            onCompletionListener?.invoke()  // 곡이 끝났을 때 실행될 코드
        }
    }

    fun playNewSong(context: Context, resId: Int) {
        release() // 기존 플레이어 해제
        mediaPlayer = MediaPlayer.create(context, resId)

        // 곡 재생 시작
        mediaPlayer?.start()
        isPlaying = true
        notifyStateChange()  // 상태 변경 알림
        startProgressUpdates()  // 진행 업데이트 시작
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

    fun setMusic(context: Context, resId: Int) {
        release()
        if (resId == 0) {
            Log.e("MusicPlayerManager", "Invalid resId passed to setMusic()")
            return
        }
        mediaPlayer = MediaPlayer.create(context, resId)
    }
}


package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SongActivity : AppCompatActivity() {

    private lateinit var seekBar: SeekBar
    private lateinit var btnPlayPause: ImageButton
    private lateinit var tvTime: TextView

    private val progressListener: (Int) -> Unit = { position ->
        val duration = MusicPlayerManager.getDuration()
        seekBar.max = duration
        seekBar.progress = position
        tvTime.text = formatTime(position)
    }

    private val stateListener: (Boolean) -> Unit = { isPlaying ->
        btnPlayPause.setImageResource(
            if (isPlaying) R.drawable.btn_miniplay_pause else R.drawable.btn_miniplayer_play
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_song)

        val songTitle = intent.getStringExtra("SONG_TITLE") ?: "Unknown Song"
        val artistName = intent.getStringExtra("ARTIST_NAME") ?: "Unknown Artist"

        findViewById<TextView>(R.id.tv_song_title).text = songTitle
        findViewById<TextView>(R.id.tv_artist_name).text = artistName
        Toast.makeText(this, "노래 제목: $songTitle\n가수 이름: $artistName", Toast.LENGTH_LONG).show()

        seekBar = findViewById(R.id.seekBar)
        btnPlayPause = findViewById(R.id.btn_play_pause)
        tvTime = findViewById(R.id.tv_time)

//        findViewById<ImageView>(R.id.iv_icon5).setOnClickListener {
//            toggleIcon(it as ImageView, R.drawable.btn_toggle_on, R.drawable.btn_toggle_off)
//        }

        // 한곡재생 버튼: 클릭 시 음악을 처음부터 재생
        findViewById<ImageView>(R.id.iv_icon1)?.setOnClickListener {
            restartSong()
        }

        btnPlayPause.setOnClickListener { togglePlayPause() }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayerManager.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        findViewById<ImageButton>(R.id.nugu_btn_down).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        MusicPlayerManager.addOnProgressUpdateListener(progressListener)
        MusicPlayerManager.addOnStateChangeListener(stateListener)
        syncUIWithPlayer()
    }

    override fun onPause() {
        super.onPause()
        MusicPlayerManager.removeOnProgressUpdateListener(progressListener)
        MusicPlayerManager.removeOnStateChangeListener(stateListener)
    }

    private fun syncUIWithPlayer() {
        val isPlaying = MusicPlayerManager.isPlaying
        val position = MusicPlayerManager.getPosition()
        val duration = MusicPlayerManager.getDuration()
        seekBar.max = duration
        seekBar.progress = position
        tvTime.text = formatTime(position)
        btnPlayPause.setImageResource(
            if (isPlaying) R.drawable.btn_miniplay_pause else R.drawable.btn_miniplayer_play
        )
    }

    private fun togglePlayPause() {
        if (MusicPlayerManager.isPlaying) {
            MusicPlayerManager.pause()
        } else {
            MusicPlayerManager.play()
        }
    }

    private fun restartSong() {
        MusicPlayerManager.seekTo(0)
        MusicPlayerManager.play()
        syncUIWithPlayer()
    }

    private fun formatTime(millis: Int): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

//    private fun toggleIcon(imageView: ImageView, activeRes: Int, inactiveRes: Int) {
//        val current = imageView.drawable.constantState
//        val active = resources.getDrawable(activeRes, null).constantState
//        imageView.setImageResource(if (current == active) inactiveRes else activeRes)
//    }
}
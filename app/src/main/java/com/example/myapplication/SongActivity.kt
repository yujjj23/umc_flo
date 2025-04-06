package com.example.myapplication

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SongActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var btnPlayPause: ImageButton
    private lateinit var tvTime: TextView
    private val handler = Handler()
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_song)

        // 데이터 받기
        val songTitle = intent.getStringExtra("SONG_TITLE") ?: "Unknown Song"
        val artistName = intent.getStringExtra("ARTIST_NAME") ?: "Unknown Artist"

        findViewById<TextView>(R.id.tv_song_title).text = songTitle
        findViewById<TextView>(R.id.tv_artist_name).text = artistName
        Toast.makeText(this, "노래 제목: $songTitle\n가수 이름: $artistName", Toast.LENGTH_LONG).show()

        // UI 요소 초기화
        seekBar = findViewById(R.id.seekBar)
        btnPlayPause = findViewById(R.id.btn_play_pause)
        tvTime = findViewById(R.id.tv_time)
        val ivIcon1: ImageView = findViewById(R.id.iv_icon1)
        val ivIcon5: ImageView = findViewById(R.id.iv_icon5)
        val ivIcon3: ImageView = findViewById(R.id.btn_play_pause)

        // MediaPlayer 설정
        mediaPlayer = MediaPlayer.create(this, R.raw.bluedream_cheel)
        seekBar.max = mediaPlayer.duration

        // 재생 및 일시정지 버튼 클릭 리스너
        btnPlayPause.setOnClickListener {
            togglePlayPause()
        }

        // 재생/일시정지 버튼 (ivIcon3) 토글
        ivIcon3.setOnClickListener {
            togglePlayPause()
        }

        // 아이콘 클릭 시 이미지 변경
        ivIcon1.setOnClickListener {
            toggleIcon(ivIcon1, R.drawable.btn_toggle_on, R.drawable.btn_toggle_off)
        }
        ivIcon5.setOnClickListener {
            toggleIcon(ivIcon5, R.drawable.btn_toggle_on, R.drawable.btn_toggle_off)
        }

        // SeekBar 변경 리스너
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 종료 버튼
        findViewById<ImageButton>(R.id.nugu_btn_down).setOnClickListener {
            finish()
        }
    }

    private fun togglePlayPause() {
        if (isPlaying) {
            pauseMusic()
        } else {
            playMusic()
        }
    }

    private fun playMusic() {
        mediaPlayer.start()
        handler.post(updateSeekBar)
        btnPlayPause.setImageResource(R.drawable.btn_miniplay_pause)
        findViewById<ImageView>(R.id.btn_play_pause).setImageResource(R.drawable.btn_miniplay_pause)
        isPlaying = true
    }

    private fun pauseMusic() {
        mediaPlayer.pause()
        handler.removeCallbacks(updateSeekBar)
        btnPlayPause.setImageResource(R.drawable.btn_miniplayer_play)
        findViewById<ImageView>(R.id.btn_play_pause).setImageResource(R.drawable.btn_miniplayer_play)
        isPlaying = false
    }

    private val updateSeekBar = object : Runnable {
        override fun run() {
            seekBar.progress = mediaPlayer.currentPosition
            tvTime.text = formatTime(mediaPlayer.currentPosition)
            handler.postDelayed(this, 500)
        }
    }

    private fun formatTime(millis: Int): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun toggleIcon(imageView: ImageView, activeRes: Int, inactiveRes: Int) {
        if (imageView.drawable.constantState == resources.getDrawable(activeRes, null).constantState) {
            imageView.setImageResource(inactiveRes)
        } else {
            imageView.setImageResource(activeRes)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(updateSeekBar)
    }
}
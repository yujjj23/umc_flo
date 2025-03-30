package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SongActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_song)  // song 화면 XML 연결

        // MainActivity에서 전달된 데이터 받기
        val songTitle = intent.getStringExtra("SONG_TITLE") ?: "Unknown Song"
        val artistName = intent.getStringExtra("ARTIST_NAME") ?: "Unknown Artist"

        val ivIcon1: ImageView = findViewById(R.id.iv_icon1)
        val ivIcon5: ImageView = findViewById(R.id.iv_icon5)

// 아이콘 1 클릭 시 이미지 변경
        ivIcon1.setOnClickListener {
            if (ivIcon1.drawable.constantState == resources.getDrawable(R.drawable.btn_toggle_on).constantState) {
                ivIcon1.setImageResource(R.drawable.btn_toggle_off) // 원래 이미지로 변경
            } else {
                ivIcon1.setImageResource(R.drawable.btn_toggle_on) // 변경된 이미지로 변경
            }
        }

// 아이콘 5 클릭 시 이미지 변경
        ivIcon5.setOnClickListener {
            if (ivIcon5.drawable.constantState == resources.getDrawable(R.drawable.btn_toggle_on).constantState) {
                ivIcon5.setImageResource(R.drawable.btn_toggle_off) // 원래 이미지로 변경
            } else {
                ivIcon5.setImageResource(R.drawable.btn_toggle_on) // 변경된 이미지로 변경
            }
        }



        // UI에 전달된 데이터 표시
        findViewById<TextView>(R.id.tv_song_title).text = songTitle
        findViewById<TextView>(R.id.tv_artist_name).text = artistName

        // 전달받은 데이터로 토스트 출력
        Toast.makeText(this, "노래 제목: $songTitle\n가수 이름: $artistName", Toast.LENGTH_LONG).show()

        // ImageButton 클릭 시 종료
        findViewById<ImageButton>(R.id.nugu_btn_down).setOnClickListener {
            finish()  // SongActivity 종료
        }
    }
}


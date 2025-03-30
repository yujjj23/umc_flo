package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // BottomNavigationView와 NavController 연결
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // AppBarConfiguration을 사용해 네비게이션에 대한 설정
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        // ActionBar와 NavController 연결
        setupActionBarWithNavController(navController, appBarConfiguration)
        // BottomNavigationView와 NavController 연결
        navView.setupWithNavController(navController)


        //// 🎵 미니 플레이어 클릭 시 SongActivity 실행
        binding.miniPlayer.setOnClickListener {
            // 실제 미니 플레이어에 표시된 텍스트 값 가져오기
            val songTitle = findViewById<TextView>(R.id.tv_song_title).text.toString()
            val artistName = findViewById<TextView>(R.id.tv_artist_name).text.toString()

            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("SONG_TITLE", songTitle) // 실제 노래 제목 전달
            intent.putExtra("ARTIST_NAME", artistName) // 실제 아티스트 이름 전달
            startActivity(intent)

        }

    }
}
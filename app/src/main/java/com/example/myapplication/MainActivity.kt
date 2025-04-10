package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var miniSeekBar: SeekBar
    private lateinit var miniBtnPlay: ImageView

    private val progressListener: (Int) -> Unit = { position ->
        val duration = MusicPlayerManager.getDuration()
        miniSeekBar.progress = position * 1000 / duration
    }

    private val stateListener: (Boolean) -> Unit = { isPlaying ->
        miniBtnPlay.setImageResource(
            if (isPlaying) R.drawable.btn_miniplay_pause else R.drawable.btn_miniplayer_play
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        MusicPlayerManager.initialize(this)

        miniSeekBar = findViewById(R.id.mini_seek_bar)
        miniBtnPlay = findViewById(R.id.mini_btn_play)

        miniSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val duration = MusicPlayerManager.getDuration()
                    val seekTo = (progress * duration) / 1000
                    MusicPlayerManager.seekTo(seekTo)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        miniBtnPlay.setOnClickListener {
            if (MusicPlayerManager.isPlaying) {
                MusicPlayerManager.pause()
            } else {
                MusicPlayerManager.play()
            }
        }

        binding.miniPlayer.setOnClickListener {
            val songTitle = findViewById<TextView>(R.id.tv_song_title).text.toString()
            val artistName = findViewById<TextView>(R.id.tv_artist_name).text.toString()
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("SONG_TITLE", songTitle)
            intent.putExtra("ARTIST_NAME", artistName)
            startActivity(intent)
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

    override fun onDestroy() {
        super.onDestroy()
        MusicPlayerManager.stopProgressUpdates()
    }

    private fun syncUIWithPlayer() {
        val isPlaying = MusicPlayerManager.isPlaying
        val position = MusicPlayerManager.getPosition()
        val duration = MusicPlayerManager.getDuration()
        miniSeekBar.progress = position * 1000 / duration
        miniBtnPlay.setImageResource(
            if (isPlaying) R.drawable.btn_miniplay_pause else R.drawable.btn_miniplayer_play
        )
    }
}

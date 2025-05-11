package com.example.myapplication

import AppDatabase
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var miniSeekBar: SeekBar
    private lateinit var miniBtnPlay: ImageView
    private lateinit var songDB: SongDatabase
    private lateinit var albumDB: AppDatabase
    private lateinit var sharedPrefs: SharedPreferences

    private var song: Song = Song()
    private var gson: Gson = Gson()

//    private lateinit var songDao: SongDao
private lateinit var songDao: SongDao
    private var currentSongIndex = 0
//    private lateinit var songs: List<Song>
private var songs: List<Song> = emptyList()


    // songs 접근을 위한 메서드
    fun setSongs(songs: List<Song>) {
        this.songs = songs
    }

    // currentSongIndex 설정 메서드
    fun setCurrentSongIndex(index: Int) {
        this.currentSongIndex = index
    }

    // 외부에서 곡 재생 요청할 수 있도록 public 함수로 만들기
    fun playSongFromOutside(song: Song) {
        playSong(song)
    }


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

        sharedPrefs = getSharedPreferences("song", MODE_PRIVATE)

        songDB = SongDatabase.getInstance(this)!! // 초기화 먼저
        songDao = SongDatabase.getInstance(this)!!.songDao()

        val albumDatabase = DatabaseProvider.getDatabase(this)
        val albumDao = albumDatabase.albumDao()

        // "다음 곡" 버튼 리스너
        binding.miniNextPlay.setOnClickListener {
            playNextSong() // 다음 곡 재생
        }

        binding.miniPreviousPlay.setOnClickListener {
            playPreviousSong()
        }

        // 예시: 초기 곡 설정 (HomeFragment에서 호출된 첫 곡)
        loadInitialSong()

        // 예시: MusicPlayerManager 설정
        MusicPlayerManager.setOnCompletionListener {
            playNextSong() // 곡이 끝나면 자동으로 다음 곡 재생
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        insertDummySongs() // ✅ 더미데이터 삽입
        initializeCurrentSong() // ✅ SharedPref → 현재 노래 설정

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
            val editor = getSharedPreferences("Song", MODE_PRIVATE).edit()
            editor.putInt("SongId", song.id)
            editor.apply()

            intent.putExtra("SONG_TITLE", songTitle)
            intent.putExtra("ARTIST_NAME", artistName)
            startActivity(intent)
        }
    }

//    private fun loadInitialSong() {
//        // 예시: 첫 번째 앨범의 첫 곡을 가져오고 초기화하는 코드
//        songs = songDao.getSongsByAlbum(1)
//        val firstSong = songs[0]
//        playSong(firstSong)
//    }
private fun loadInitialSong() {
//    songs = songDao.getSongsByAlbum(song.albumIdx)

    if (songs.isNotEmpty()) {
        currentSongIndex = 0
        val currentSong = songs[currentSongIndex]
        playSong(currentSong)
    } else {
        Toast.makeText(this, "앨범에 노래가 없습니다.", Toast.LENGTH_SHORT).show()
    }
}

    private fun playSong(song: Song) {
        val resId = resources.getIdentifier("bluedream_cheel", "raw", this.packageName)

        MusicPlayerManager.playNewSong(this, resId)

        binding.tvSongTitle.text = song.title
        binding.tvArtistName.text = song.singer
        binding.miniBtnPlay.setImageResource(R.drawable.btn_miniplay_pause)
        binding.miniPlayer.visibility = View.VISIBLE

        this.song = song
        song.isPlaying = true
//        songDao.update(song)
    }

    private fun playNextSong() {
        // 현재 곡이 속한 앨범의 곡들만 가져오기
        songs = songDao.getSongsByAlbum(song.albumIdx)

        if (songs.isNotEmpty() && currentSongIndex < songs.size - 1) {
            currentSongIndex++  // 다음 곡으로 인덱스 변경
            val nextSong = songs[currentSongIndex]
            playSong(nextSong)  // 다음 곡 재생
        } else {
            Toast.makeText(this, "다음 곡이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playPreviousSong() {
        // 현재 곡이 속한 앨범의 곡들만 가져오기
        songs = songDao.getSongsByAlbum(song.albumIdx)

        if (songs.isNotEmpty() && currentSongIndex > 0) {
            currentSongIndex--  // 이전 곡으로 인덱스 변경
            val previousSong = songs[currentSongIndex]
            playSong(previousSong)  // 이전 곡 재생
        } else {
            Toast.makeText(this, "이전 곡이 없습니다.", Toast.LENGTH_SHORT).show()
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

    // MainActivity에서의 onCreate 또는 onStart에서 호출
    override fun onStart() {
        super.onStart()

        // Album 삽입
        val albumDatabase = DatabaseProvider.getDatabase(this)
        val albumDao = albumDatabase.albumDao()

        insertDummyAlbums(albumDao)  // 여기서 DB에 삽입

        // 기존 코드
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = spf.getString("SongData", null)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        song = if (songId == 0) {
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())
    }

    // 데이터 삽입 함수
    private fun insertDummyAlbums(albumDao: AlbumDao) {
        val album1 = Album(
            id = 1,
            title = "Next Level",
            singer = "aespa",
            coverImg = "img_album_exp3"
        )
        val album2 = Album(
            id = 2,
            title = "Butter",
            singer = "BTS",
            coverImg = "img_album_exp4"
        )

        lifecycleScope.launch {
            albumDao.insertAlbum(album1)  // suspend 함수 호출
            albumDao.insertAlbum(album2)  // suspend 함수 호출
        }
    }

    private fun insertDummySongs() {
        CoroutineScope(Dispatchers.IO).launch {
            val songDB = SongDatabase.getInstance(this@MainActivity)
            val songs = songDB!!.songDao().getSongs()

            if (songs.isNotEmpty()) return@launch

            songDB.songDao().insert(
                Song(
                    "Lilac", "IU", 0, 200, false,
                    "music_lilac", coverImg = "img_album_exp2", isLike = false, 1
                )
            )

            songDB.songDao().insert(
                Song(
                    "Flu", "IU", 0, 200, false,
                    "music_flu", coverImg = "img_album_exp2", isLike = false, 1
                )
            )

            songDB.songDao().insert(
                Song(
                    "Butter", "bts", 0, 190, false,
                    "music_butter", coverImg = "img_album_exp4", isLike = false, 2
                )
            )

            songDB.songDao().insert(
                Song(
                    "Next Level", "aespa", 0, 210, false,
                    "music_next", coverImg = "img_album_exp3", isLike = false, 1
                )
            )

            songDB.songDao().insert(
                Song(
                    "Boy with Luv", "bts", 0, 230, false,
                    "music_boy", coverImg = "img_album_exp4", isLike = false, 2
                )
            )

            songDB.songDao().insert(
                Song(
                    "BBoom BBoom", "모모랜드 (momoland)", 0, 240, false,
                    "music_bbom", coverImg = "img_album_exp", isLike = false, 2
                )
            )

            val insertedSongs = songDB.songDao().getSongs()
            Log.d("DB data", insertedSongs.toString())
        }
    }

    private fun initializeCurrentSong() {
        val songId = sharedPrefs.getInt("songId", 1)
        val currentSong = songDB.songDao().getSongById(songId) ?: return

        findViewById<TextView>(R.id.tv_song_title).text = currentSong.title
        findViewById<TextView>(R.id.tv_artist_name).text = currentSong.singer
    }
}

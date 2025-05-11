package com.example.myapplication

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar


class SongActivity : AppCompatActivity() {

    private lateinit var ivLeftImage: ImageView // 좋아요 버튼
    private var isLiked = false

    private lateinit var seekBar: SeekBar
    private lateinit var btnPlayPause: ImageButton
    private lateinit var tvTime: TextView
    private lateinit var tvSongTitle: TextView
    private lateinit var tvArtistName: TextView
    private lateinit var btnRestart: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var btnPrevious: ImageView
    private lateinit var btnClose: ImageButton

    private val songs = arrayListOf<Song>()
//    private var songs: List<Song> = emptyList()

    private lateinit var songDB: SongDatabase
    private var nowPos = 0

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

        // View 초기화
        seekBar = findViewById(R.id.seekBar)
        btnPlayPause = findViewById(R.id.btn_play_pause)
        tvTime = findViewById(R.id.tv_time)
        tvSongTitle = findViewById(R.id.tv_song_title)
        tvArtistName = findViewById(R.id.tv_artist_name)
        btnRestart = findViewById(R.id.iv_icon1)
        btnNext = findViewById(R.id.iv_icon4)
        btnPrevious = findViewById(R.id.iv_icon2)
        btnClose = findViewById(R.id.nugu_btn_down)
        ivLeftImage = findViewById(R.id.iv_left_image) // 좋아요 버튼 연결

        val songTitle = intent.getStringExtra("SONG_TITLE") ?: "Unknown Song"
        val artistName = intent.getStringExtra("ARTIST_NAME") ?: "Unknown Artist"
        tvSongTitle.text = songTitle
        tvArtistName.text = artistName
        Toast.makeText(this, "노래 제목: $songTitle\n가수 이름: $artistName", Toast.LENGTH_LONG).show()

        initPlayList()
        initSong()

        btnPlayPause.setOnClickListener { togglePlayPause() }
        btnRestart.setOnClickListener { restartSong() }
        btnNext.setOnClickListener { moveSong(1) }
        btnPrevious.setOnClickListener { moveSong(-1) }
        btnClose.setOnClickListener { finish() }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) MusicPlayerManager.seekTo(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // ❤️ 좋아요 버튼 클릭 이벤트
        ivLeftImage.setOnClickListener {
            val song = songs[nowPos]
            song.isLike = !song.isLike


            // 🔄 Firebase에 저장
            LikeManager.saveLikeToFirebase(song.id, song.isLike)

            // DB 업데이트
            Thread {
                songDB.songDao().update(song)
            }.start()


            // UI 반영
            if (song.isLike) {
                ivLeftImage.setImageResource(R.drawable.ic_my_like_on)
//                Toast.makeText(this, "좋아요가 등록되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                ivLeftImage.setImageResource(R.drawable.ic_my_like_off)
                showCustomSnackbar("좋아요가 취소되었습니다.")
            }

            // 좋아요 상태 변경 후 SavedSongsFragment 갱신
            val savedSongsFragment = supportFragmentManager.findFragmentByTag("SavedSongsFragment") as? SavedSongsFragment
            savedSongsFragment?.loadSavedSongs() // 데이터 갱신
        }
    }

    private fun showCustomSnackbar(message: String) {
        // Snackbar 생성
        val rootView = findViewById<View>(android.R.id.content) // Activity의 루트 뷰
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)

        // 배경 색상 변경
        val blueColor = ContextCompat.getColor(this, R.color.my_toast_blue)
        snackbar.view.setBackgroundColor(blueColor)

        // 텍스트 색상 변경
        val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)

        // Snackbar의 위치 변경 (중앙에 배치하기 위해 레이아웃을 조정)
        val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = 0 // 하단 여백을 0으로 설정하여 중앙에 위치시킴
        snackbar.view.layoutParams = params

        // Snackbar 표시
        snackbar.show()
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

        val sharedPreferences = getSharedPreferences("Song", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("SongId", songs[nowPos].id)
            apply()
        }
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

//    private fun initPlayList() {
//        songs.clear()
//        songs.addAll(
//            listOf(
//                Song(1, "Blue Dream", "Cheel", 120, 120, false, "bluedream_cheel", "img_album1", false, 1),
//                Song(2, "Lofi Beat", "Milo", 150, 150, false, "lofi_milo", "img_album2", false, 1),
//                Song(3, "Jazz Night", "Noir", 180, 180, false, "jazz_noir", "img_album3", false, 2)
//            )
//        )
//    }


    private fun initSong() {
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val savedSongId = sharedPreferences.getInt("SongId", 0)
        nowPos = getPlayingSongPosition(savedSongId)
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        return songs.indexOfFirst { it.id == songId }.takeIf { it != -1 } ?: 0
    }

    private fun setPlayer(song: Song) {
        tvSongTitle.text = song.title
        tvArtistName.text = song.singer

        val albumImageView = findViewById<ImageView>(R.id.iv_center_image)
        val imageResId = resources.getIdentifier(song.coverImg, "drawable", packageName)
        albumImageView.setImageResource(
            if (imageResId != 0) imageResId else R.drawable.img_album_exp2
        )

        // 🎵 음악 설정
        val musicResId = resources.getIdentifier(song.music, "raw", packageName)
        MusicPlayerManager.setMusic(this, musicResId)
        MusicPlayerManager.seekTo(song.second * 1000)
        setPlayerStatus(song.isPlaying)

        // ❤️ 좋아요 상태 UI 반영
//        if (song.isLike) {
//            ivLeftImage.setImageResource(R.drawable.ic_my_like_on)
//        } else {
//            ivLeftImage.setImageResource(R.drawable.ic_my_like_off)
//        }

        // ❤️ 좋아요 상태 불러오기 (Firebase에서)
//        LikeManager.getLikeFromFirebase(song.id) { isLiked ->
//            song.isLike = isLiked
//            ivLeftImage.setImageResource(
//                if (isLiked) R.drawable.ic_my_like_on else R.drawable.ic_my_like_off
//            )
//        }

        // ❤️ 좋아요 상태를 Firebase에서 가져오기
        LikeManager.getLikeFromFirebase(song.id) { isLiked ->
            song.isLike = isLiked // 모델에 반영

            // ✅ Room에도 동기화
            Thread {
                songDB.songDao().update(song)
            }.start()

            // ✅ UI에 반영
            ivLeftImage.setImageResource(
                if (isLiked) R.drawable.ic_my_like_on else R.drawable.ic_my_like_off
            )
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        songs[nowPos].isPlaying = isPlaying
        if (isPlaying) {
            MusicPlayerManager.play()
        } else {
            MusicPlayerManager.pause()
        }
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

    private fun moveSong(direction: Int) {
        val newPos = nowPos + direction
        if (newPos in songs.indices) {
            nowPos = newPos
            setPlayer(songs[nowPos])
        } else {
            Toast.makeText(this, "더 이상 곡이 없습니다.", Toast.LENGTH_SHORT).show()
        }
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

    private fun formatTime(millis: Int): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}

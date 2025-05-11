package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song(
    val title: String = "",
    val singer: String = "",
    val second: Int = 0,        // 재생 시간 (초 단위)
    val playTime: Int = 0,   // 예: "03:30"
    var isPlaying: Boolean = false,
    val music: String = "bluedream_cheel",
    var coverImg: String = "default_cover",
    var isLike: Boolean = false,
    val albumIdx: Int = 0,   // AlbumTable의 id 값을 참조

    @PrimaryKey(autoGenerate = true) var id: Int = 0
)





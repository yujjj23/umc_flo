package com.example.myapplication

data class FirebaseSong(
    var id: Int = 0,
    val title: String = "",
    val singer: String = "",
    val second: Int = 0,
    val playTime: Int = 0,
    var isPlaying: Boolean = false,
    val music: String = "bluedream_cheel",
    var coverImg: String = "default_cover",
    var isLike: Boolean = false,
    val albumIdx: Int = 0
)


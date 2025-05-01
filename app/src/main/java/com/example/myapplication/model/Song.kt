package com.example.myapplication.model

data class Song(
    val title: String,
    val artist: String,
    val albumResId: Int,
    var isChecked: Boolean = false // 스위치 상태를 위한 필드 추가
)


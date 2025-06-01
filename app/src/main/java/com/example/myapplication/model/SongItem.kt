package com.example.myapplication.model

//data class SongItem(
//    val title: String,
//    val artist: String,
//    val albumResId: Int,
//    var isChecked: Boolean = false // 스위치 상태를 위한 필드 추가
//)

data class SongItem(
    val title: String,
    val artist: String,
    val albumResId: Int,
    var isChecked: Boolean = false, // 스위치 상태
    var isLike: Boolean = true      // 좋아요 여부
) {
    fun setIsLike(value: Boolean) {
        isLike = value
    }
}



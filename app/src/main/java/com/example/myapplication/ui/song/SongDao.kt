package com.example.myapplication.ui.song

import androidx.room.*
import com.example.myapplication.data.entities.Song

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song
    )
    @Query("SELECT * FROM SongTable")
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id=:id")
    fun getSong(id:Int): Song

    @Query("SELECT * FROM SongTable WHERE id = :songId")
    fun getSongById(songId: Int): Song?

    // 좋아요한 곡만 가져오기
    @Query("SELECT * FROM SongTable WHERE isLike = 1")
    fun getLikedSongs(): List<Song>

    // 모든 곡의 isLike 값을 false로 업데이트
    @Query("UPDATE SongTable SET isLike = 0")
    fun updateAllSongsIsLikeFalse()

    @Query("DELETE FROM SongTable WHERE isLike = 1") // isLike가 true인 곡만 삭제
    fun deleteAllLikedSongs()

    @Query("SELECT * FROM SongTable WHERE albumIdx = :albumIdx")
    fun getSongsByAlbum(albumIdx: Int): List<Song>

}

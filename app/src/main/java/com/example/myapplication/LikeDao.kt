package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like: Like)

    @Query("SELECT * FROM LikeTable WHERE userId = :userId AND isLike = 1")
    suspend fun getLikedAlbums(userId: Int): List<Like>

    @Query("SELECT * FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    suspend fun getLike(userId: Int, albumId: Int): Like?

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    suspend fun deleteLike(userId: Int, albumId: Int)

//    @Query("SELECT albumId FROM LikeTable WHERE userId = :userId AND isLike = 1")
//    fun getLikedAlbumIds(userId: Int): List<Int>

    @Query("SELECT albumId FROM LikeTable WHERE userId = :userId AND isLike = 1")
    suspend fun getLikedAlbumIds(userId: Int): List<Int>


}

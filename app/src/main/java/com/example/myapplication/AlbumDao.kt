package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(album: Album)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(albums: List<Album>)

    @Query("SELECT * FROM AlbumTable")
    fun getAllAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :albumId")
    fun getAlbumById(albumId: Int): Album?

    @Query("SELECT * FROM AlbumTable WHERE isLike = 1")
    fun getLikedAlbums(): List<Album>

    @Query("UPDATE AlbumTable SET isLike = :isLike WHERE id = :albumId")
    fun updateAlbumLike(albumId: Int, isLike: Boolean)

    @Update
    fun updateAlbum(album: Album)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAlbum(album: Album)

    @Query("SELECT * FROM AlbumTable WHERE id IN (:albumIds)")
    fun getAlbumsByIds(albumIds: List<Int>): List<Album>

    @Update
    suspend fun update(album: Album)


}





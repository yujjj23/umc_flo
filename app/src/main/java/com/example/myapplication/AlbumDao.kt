package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//@Dao
//interface AlbumDao {
//    @Insert
//    fun insert(album: Album)
//
//    @Insert
//    suspend fun insertAlbum(album: Album)
//
//    @Query("SELECT * FROM AlbumTable")
//    fun getAllAlbums(): List<Album>
//}

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album)

    @Query("SELECT COUNT(*) FROM AlbumTable WHERE id = :albumId")
    suspend fun isAlbumExist(albumId: Int): Int

    suspend fun insertAlbumIfNotExist(album: Album) {
        val count = isAlbumExist(album.id)
        if (count == 0) {
            insertAlbum(album)  // 존재하지 않으면 삽입
        }
    }
    @Query("SELECT * FROM AlbumTable")
    suspend fun getAllAlbums(): List<Album>
}



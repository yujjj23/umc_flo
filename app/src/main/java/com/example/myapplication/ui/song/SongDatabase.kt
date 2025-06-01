package com.example.myapplication.ui.song

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.firebase.LikeDao
import com.example.myapplication.data.local.UserDao
import com.example.myapplication.data.entities.Album
import com.example.myapplication.data.entities.Like
import com.example.myapplication.data.entities.Song
import com.example.myapplication.data.entities.User
import com.example.myapplication.ui.album.AlbumDao

//@Database(entities = [Song::class, User::class, Like:: class], version = 1, exportSchema = false)
//abstract class SongDatabase : RoomDatabase() {
//    abstract fun songDao(): SongDao
//    abstract fun userDao(): UserDao
//    abstract fun likeDao(): LikeDao
//
//    companion object {
//        private var instance: SongDatabase? = null
//
//        @Synchronized
//        fun getInstance(context: Context): SongDatabase? {
//            if (instance == null) {
//                synchronized(SongDatabase::class) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        SongDatabase::class.java,
//                        "song-database"
//                    )
//                        .allowMainThreadQueries()
//                        .build()
//                }
//            }
//
//            return instance
//        }
//    }
//}

@Database(
    entities = [Song::class, User::class, Album::class, Like::class],
    version = 1,
    exportSchema = false
)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun userDao(): UserDao
    abstract fun albumDao(): AlbumDao
    abstract fun likeDao(): LikeDao

    companion object {
        @Volatile private var instance: SongDatabase? = null

        fun getInstance(context: Context): SongDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    "song_database"
                )
                    .allowMainThreadQueries() // 가능하면 별도 스레드에서 DB 작업 권장
                    .build()
                instance = newInstance
                newInstance
            }
        }
    }
}

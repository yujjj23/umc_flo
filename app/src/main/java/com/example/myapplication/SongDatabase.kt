package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

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

//package com.example.myapplication
//
//// DatabaseProvider.kt
//import AppDatabase
//import android.content.Context
//import androidx.room.Room
//
//object DatabaseProvider {
//    private var database: AppDatabase? = null
//
//    fun getDatabase(context: Context): AppDatabase {
//        if (database == null) {
//            database = Room.databaseBuilder(
//                context.applicationContext,
//                AppDatabase::class.java,
//                "album_database"
//            ).build()
//        }
//        return database!!
//    }
//}

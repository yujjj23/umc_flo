package com.example.myapplication.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.ui.song.SongDao
import com.example.myapplication.ui.song.SongDatabase


class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val songDao: SongDao = requireNotNull(SongDatabase.getInstance(application)).songDao()


    fun deleteAllLikedSongs() {
        Thread {
            songDao.deleteAllLikedSongs()
        }.start()
    }
}


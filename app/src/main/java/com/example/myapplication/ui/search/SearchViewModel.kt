package com.example.myapplication.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.SongDao
import com.example.myapplication.SongDatabase
import com.example.myapplication.model.SongItem


class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val songDao: SongDao = requireNotNull(SongDatabase.getInstance(application)).songDao()


    fun deleteAllLikedSongs() {
        Thread {
            songDao.deleteAllLikedSongs()
        }.start()
    }
}


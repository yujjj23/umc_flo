package com.example.myapplication.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    val title: String? = "",
    val singer: String? = "",
    var coverImg: String = "default_cover",
    var isLike: Boolean = false
) : Parcelable



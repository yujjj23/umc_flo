package com.example.myapplication

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//@Entity(tableName = "LikeTable")
//data class Like(
//    var userId: Int,
//    var albumId: Int
//){
//    @PrimaryKey(autoGenerate = true) var id:Int=0
//}

@Entity(
    tableName = "LikeTable",
    primaryKeys = ["userId", "albumId"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"]),
        ForeignKey(entity = Album::class, parentColumns = ["id"], childColumns = ["albumId"])
    ]
)
data class Like(
    val userId: Int,
    val albumId: Int,
    val isLike: Boolean
)

package com.example.myapplication

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    val title: String? = "",
    val singer: String? = "",
    var coverImg: String = "default_cover"
) : Parcelable {

    // Parcelable constructor
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        title = parcel.readString(),
        singer = parcel.readString(),
        coverImg = parcel.readString() ?: "default_cover"
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(singer)
        parcel.writeString(coverImg)
    }

    override fun describeContents(): Int = 0

    // Companion object to create the Album from a Parcel
    companion object CREATOR : Parcelable.Creator<Album> {
        override fun createFromParcel(parcel: Parcel): Album {
            return Album(parcel)
        }

        override fun newArray(size: Int): Array<Album?> {
            return arrayOfNulls(size)
        }
    }
}




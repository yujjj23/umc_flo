package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User(
    var email:String,
    var password:String
){
    @PrimaryKey(autoGenerate = true)var id: Int=0
}

//@Entity(tableName = "UserTable")
//data class User(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val email: String,
//    val password: String
//)

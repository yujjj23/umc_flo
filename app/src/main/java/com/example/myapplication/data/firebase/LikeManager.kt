package com.example.myapplication.data.firebase

import com.google.firebase.database.FirebaseDatabase

object LikeManager {

    fun saveLikeToFirebase(songId: Int, isLiked: Boolean) {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("likes").child(songId.toString())
        val likeData = mapOf("songId" to songId, "isLiked" to isLiked)
        ref.setValue(likeData)
    }

    fun getLikeFromFirebase(songId: Int, callback: (Boolean) -> Unit) {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("likes").child(songId.toString())
        ref.get().addOnSuccessListener { snapshot ->
            val isLiked = snapshot.child("isLiked").getValue(Boolean::class.java) ?: false
            callback(isLiked)
        }.addOnFailureListener {
            callback(false)
        }
    }

    fun getLikedSongs(callback: (List<Int>) -> Unit) {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("likes")
        ref.get().addOnSuccessListener { snapshot ->
            val likedIds = mutableListOf<Int>()
            snapshot.children.forEach {
                val isLiked = it.child("isLiked").getValue(Boolean::class.java) ?: false
                if (isLiked) likedIds.add(it.key?.toIntOrNull() ?: -1)
            }
            callback(likedIds.filter { it != -1 })
        }
    }

    fun clearAllLikes(callback: () -> Unit) {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("likes")

        ref.get().addOnSuccessListener { snapshot ->
            val updates = mutableMapOf<String, Any>()
            snapshot.children.forEach { songSnapshot ->
                val songId = songSnapshot.key ?: return@forEach
                updates["$songId/isLiked"] = false
            }

            ref.updateChildren(updates).addOnSuccessListener {
                callback()
            }
        }
    }


}

package com.example.myapplication

data class JoinResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Timestamp
)

data class Timestamp(
    val memberId: Int,
    val createdAt: String,
    val updatedAt: String,
)

package com.example.myapplication.data.remote

data class TestResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?
)

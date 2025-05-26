package com.example.myapplication

data class TestResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?
)

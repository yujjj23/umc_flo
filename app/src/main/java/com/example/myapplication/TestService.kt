package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET

interface TestService {
    @GET("/test")
    fun testApi(): Call<TestResponse>
}

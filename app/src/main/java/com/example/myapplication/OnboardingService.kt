package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OnboardingService {
    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/join")
    fun signUp(@Body request: SignUpRequest): Call<JoinResponse>
}

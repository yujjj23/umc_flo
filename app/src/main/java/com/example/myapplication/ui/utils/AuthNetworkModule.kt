package com.example.myapplication.ui.utils//package com.example.myapplication
//

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthNetworkModule {

    private var accessToken: String? = null

    // 토큰 저장 함수
    fun setAccessToken(token: String) {
        accessToken = token
    }

    // Retrofit 클라이언트 반환 함수
    fun getClient(): Retrofit {
        // OkHttp 인터셉터로 모든 요청에 Authorization 헤더 붙이기
        val authInterceptor = Interceptor { chain ->
            val originalRequest: Request = chain.request()
            val builder = originalRequest.newBuilder()

            accessToken?.let {
                builder.addHeader("Authorization", "Bearer $it")
            }

            val newRequest = builder.build()
            chain.proceed(newRequest)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://aos.inyro.site/")  // 실제 API 기본 URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}

package com.example.myapplication.data.remote

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "6b1648c1637adde701966e3f1e4b1952") // 네이티브 앱 키
    }
}


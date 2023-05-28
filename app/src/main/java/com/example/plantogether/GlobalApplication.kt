package com.example.plantogether

import android.app.Application
import com.example.plantogether.R
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, this.getString(R.string.kakao_native_app_key))
    }
}
package com.example.matheasyapp.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://api.openai.com/v1/chat/"

    // Tạo một OkHttpClient với thời gian chờ 1 phút
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS) // Thời gian chờ kết nối
        .writeTimeout(60, TimeUnit.SECONDS)    // Thời gian chờ ghi
        .readTimeout(60, TimeUnit.SECONDS)     // Thời gian chờ đọc
        .build()



    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Sử dụng OkHttpClient tùy chỉnh
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}

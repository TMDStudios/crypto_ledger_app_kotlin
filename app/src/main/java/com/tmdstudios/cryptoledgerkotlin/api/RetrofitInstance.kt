package com.tmdstudios.cryptoledgerkotlin.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: API by lazy {
        Retrofit.Builder()
            .baseUrl("https://cls-prod-cls-z2mvyu.mo1.mogenius.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)
    }
}
package com.tmdstudios.cryptoledgerkotlin.tools

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface API {
    @GET("/api/get-user-ledger/{APIKey}")
    suspend fun getLedger(@Path("APIKey") APIKey: String): Response<List<LedgerCoin>>

    @GET("/api/get-prices")
    suspend fun getPrices(): Response<List<Coin>>

//    @POST
//    fun buyCoin(@Body coin: Coin): Response<BuyCoin>
}
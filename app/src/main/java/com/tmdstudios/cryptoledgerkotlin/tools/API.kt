package com.tmdstudios.cryptoledgerkotlin.tools

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface API {
    @GET("/api/get-user-ledger/{APIKey}")
    suspend fun getLedger(@Path("APIKey") APIKey: String): Response<List<LedgerCoin>>

    @GET("/api/get-prices")
    suspend fun getPrices(): Response<List<Coin>>

    @POST("/api/buy-coin-api/{APIKey}")
    suspend fun buyCoin(@Path("APIKey") APIKey: String, @Body coin: BuyCoin): Response<BuyCoin>

    @POST("/api/sell-coin-api/{APIKey}")
    suspend fun sellCoin(@Path("APIKey") APIKey: String, @Body coin: SellCoin): Response<SellCoin>
}
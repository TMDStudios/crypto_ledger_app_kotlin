package com.tmdstudios.cryptoledgerkotlin.tools

import retrofit2.Response
import retrofit2.http.GET

interface API {
    @GET("/api/get-user-ledger/9dcafb7b8028a23bba9e8197ec032a6ef91ebd18")
    suspend fun getLedger(): Response<List<LedgerCoin>>

    @GET("/api/get-prices")
    suspend fun getPrices(): Response<List<Coin>>

//    @POST
//    fun buyCoin(@Body coin: Coin): Response<BuyCoin>
}
package com.tmdstudios.cryptoledgerkotlin

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface API {
    @GET("/api/get-user-ledger/9dcafb7b8028a23bba9e8197ec032a6ef91ebd18")
    suspend fun getLedger(): Response<List<LedgerCoin>>

//    @POST
//    fun buyCoin(@Body coin: Coin): Response<BuyCoin>
}
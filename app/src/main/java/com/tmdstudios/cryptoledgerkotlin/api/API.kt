package com.tmdstudios.cryptoledgerkotlin.api

import com.tmdstudios.cryptoledgerkotlin.models.Coin
import com.tmdstudios.cryptoledgerkotlin.models.LedgerCoin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface API {
    @GET("/api/user-ledger/{APIKey}")
    suspend fun getLedger(@Path("APIKey") APIKey: String): Response<List<LedgerCoin>>

    @GET("/api/coins")
    suspend fun getPrices(): Response<List<Coin>>
}
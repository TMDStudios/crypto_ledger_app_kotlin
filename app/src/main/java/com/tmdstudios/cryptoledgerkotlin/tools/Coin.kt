package com.tmdstudios.cryptoledgerkotlin.tools

data class Coin(
    val id: Int,
    val name: String,
    val price: String,
    val price_1h: Double,
    val price_24h: Double,
    val price_btc: String,
    val price_eth: String,
    val symbol: String
)
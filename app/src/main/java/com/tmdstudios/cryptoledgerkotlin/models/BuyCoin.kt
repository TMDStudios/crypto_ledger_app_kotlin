package com.tmdstudios.cryptoledgerkotlin.models

data class BuyCoin (
    val name: String,
    val symbol: String,
    val amount: Double,
    val purchasePrice: Double
    )
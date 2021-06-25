package com.tmdstudios.cryptoledgerkotlin.tools

data class LedgerCoin(
    val _purchase_price: String,
    val amount: String,
    val current_price: String,
    val custom_price: String,
    val date_bought: String,
    val date_sold: Any,
    val gain: Any,
    val id: Int,
    val merged: Boolean,
    val name: String,
    val owner: Int,
    val price_difference: Double,
    val sell_amount: String,
    val sell_price: Any,
    val sold: Boolean,
    val total_amount: String,
    val total_profit: String,
    val total_spent: String,
    val total_value: String,
    val value: String
)
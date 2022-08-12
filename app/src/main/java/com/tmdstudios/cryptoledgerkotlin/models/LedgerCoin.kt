package com.tmdstudios.cryptoledgerkotlin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LedgerCoin(
    var id: Int,
    var name: String,
    var symbol: String,
    var dateBought: String,
    var dateSold: String?,
    var updatedAt: String?,
    var sellPrice: Double,
    var sellAmount: Double,
    var totalSpent: Double,
    var merged: Boolean,
    var sold: Boolean,
    var gain: Double,
    var amount: Double,
    var totalAmount: Double,
    var value: Double,
    var totalValue: Double,
    var customPrice: Double,
    var purchasePrice: Double,
    var currentPrice: Double,
    var priceDifference: Double,
    var totalProfit: Double
): Parcelable
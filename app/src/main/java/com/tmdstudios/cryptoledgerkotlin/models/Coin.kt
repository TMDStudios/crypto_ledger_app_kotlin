package com.tmdstudios.cryptoledgerkotlin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coin(
    val name: String,
    val symbol: String,
    val logo: String,
    val price: String,
    val coinRank: Double,
    val priceChangePercentage1d: Double,
    val priceChangePercentage7d: Double,
    val priceChangePercentage30d: Double
): Parcelable
package com.ioline.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Instrument(
    val classCode: String,
    val figi: String,
    val first1dayCandleDate: String,
    val first1minCandleDate: String,
    val forIisFlag: Boolean,
    val instrumentType: String,
    val name: String,
    val ticker: String,
    val uid: String,
    val price: Double = 0.0
)
package com.ioline.data.models

import kotlinx.serialization.Serializable
import org.roboquant.common.Asset

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

fun Asset.toInstrument(): Instrument = Instrument(
    classCode = "",
    figi = symbol,
    first1dayCandleDate = "first1dayCandleDate",
    first1minCandleDate = "first1minCandleDate",
    forIisFlag = false,
    instrumentType = type.toString(),
    name = symbol,
    ticker = symbol,
    uid = id,
)
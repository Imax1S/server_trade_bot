package com.ioline.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Operation(
    val type: OrderType,
    val asset: Instrument,
    val price: Double,
    val date: String,
    val size: Int,
    val pnlValue: Double
)

enum class OrderType {
    SELL,
    BUY
}

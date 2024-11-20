package com.ioline.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Operation(
    val type: OrderType,
    val asset: Instrument,
    val price: Double,
    val date: String,
)

enum class OrderType {
    SELL,
    BUY
}

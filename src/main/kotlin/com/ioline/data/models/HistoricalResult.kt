package com.ioline.data.models

import kotlinx.serialization.Serializable

@Serializable
data class HistoricalResult(
    val finalBalance: Double,
    val yield: Double,
    val history: List<Double> = emptyList()
)

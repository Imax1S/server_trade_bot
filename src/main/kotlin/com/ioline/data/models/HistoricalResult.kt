package com.ioline.data.models

import kotlinx.serialization.Serializable

@Serializable
data class HistoricalResult(
    val finalBalance: Double,
    val yield: Double = 0.0,
    val history: List<Double> = emptyList(),
    val operations: List<Operation> = emptyList()
)
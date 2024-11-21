package com.ioline.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Strategy(
    val type: StrategyType,
    val description: String = "",
    val param1: String,
    val param2: String,
)

val strategiesStorage = mutableListOf<Strategy>()
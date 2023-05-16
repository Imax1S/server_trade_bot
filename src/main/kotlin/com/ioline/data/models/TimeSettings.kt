package com.ioline.data.models

import com.ioline.tradebot.data.models.TimePeriod
import kotlinx.serialization.Serializable
import ru.tinkoff.piapi.contract.v1.CandleInterval

@Serializable
data class TimeSettings(
    val interval: CandleInterval,
    val start: String,
    val end: String,
    val period: TimePeriod
)

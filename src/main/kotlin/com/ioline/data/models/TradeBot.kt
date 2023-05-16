package com.ioline.data.models

import com.ioline.tradebot.data.models.TimePeriod
import kotlinx.serialization.Serializable
import ru.tinkoff.piapi.contract.v1.CandleInterval

@Serializable
data class TradeBot(
    val id: String,
    val name: String,
    val strategy: Strategy,
    var isActive: Boolean = false,
    val instrumentsFIGI: List<String> = emptyList(),
    val marketEnvironment: MarketEnvironment,
    val timeSettings: TimeSettings,
    val result: HistoricalResult? = null
)

val tradeBotsStorage = mutableListOf(
    TradeBot(
        id = "1",
        name = "First One!",
        strategy = Strategy(StrategyType.EMA, "5", "15"),
        isActive = false,
        instrumentsFIGI = listOf(
            "TCSS09805522", //yndx
            "BBG00QPYJ5H0" //tcsg
        ),
        marketEnvironment = MarketEnvironment.HISTORICAL_DATA,
        timeSettings = TimeSettings(
            CandleInterval.CANDLE_INTERVAL_HOUR,
            "",
            "",
            TimePeriod.DAYS
        )
    )
)

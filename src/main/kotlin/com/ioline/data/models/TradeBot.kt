package com.ioline.data.models

import com.ioline.tradebot.data.models.TimePeriod
import kotlinx.serialization.Serializable
import ru.tinkoff.piapi.contract.v1.CandleInterval

@Serializable
data class TradeBot(
    val id: String,
    val name: String,
    val description: String = "",
    val strategy: Strategy,
    var isActive: Boolean = false,
    val instrumentsFIGI: List<String> = emptyList(),
    val marketEnvironment: MarketEnvironment,
    val timeSettings: TimeSettings,
    val result: HistoricalResult? = null,
    val deals: List<Operation> = emptyList(),
    val assets: List<Instrument> = emptyList()
)

val tradeBotsStorage = mutableListOf(
    TradeBot(
        id = "1",
        name = "EMA Strategy",
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
    ),
    TradeBot(
        id = "2",
        name = "RSI Strategy",
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
    ),
    TradeBot(
        id = "3",
        name = "Custom Strategy",
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

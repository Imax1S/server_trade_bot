package com.ioline.data.models

import com.ioline.tradebot.data.models.OperationMode
import kotlinx.serialization.Serializable
import ru.tinkoff.piapi.contract.v1.CandleInterval

@Serializable
data class TradeBot(
    val id: String = "0",
    val name: String,
    val description: String = "",
    val strategy: Strategy? = null,
    var isActive: Boolean = false,
    val instrumentsFIGI: List<String> = emptyList(),
    val marketEnvironment: MarketEnvironment,
    val timeSettings: TimeSettings? = null,
    val mode: OperationMode = OperationMode.MANUAL,
    val result: HistoricalResult? = null,
    val operations: List<Operation> = emptyList(),
    val assets: List<Instrument> = emptyList()
)

val tradeBotsStorage = mutableListOf(
    TradeBot(
        id = "1",
        name = "EMA Strategy",
        strategy = Strategy(
            type = StrategyType.EMA,
            param1 = "5",
            param2 = "15"
        ),
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
        strategy = Strategy(type = StrategyType.EMA, param1 = "5", param2 = "15"),
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
        strategy = Strategy(
            type = StrategyType.EMA,
            param1 = "5",
            param2 = "15"
        ),
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

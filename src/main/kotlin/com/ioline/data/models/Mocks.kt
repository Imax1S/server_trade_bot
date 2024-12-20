package com.ioline.data.models

import ru.tinkoff.piapi.contract.v1.CandleInterval

val tradeBotsStorage = mutableListOf(
    TradeBot(
        id = "1",
        name = "EMA Strategy",
        description = "Super bot, which uses super power",
        strategy = Strategy(
            type = StrategyType.EMA,
            param1 = "5",
            param2 = "15"
        ),
        isActive = false,
        instrumentsFIGI = listOf(
            "TCSS09805522", //yndx
//            "BBG00QPYJ5H0" //tcsg
        ),
        marketEnvironment = MarketEnvironment.HISTORICAL_DATA,
        timeSettings = TimeSettings(
            CandleInterval.CANDLE_INTERVAL_DAY,
            "",
            "",
            TimePeriod.DAYS
        ),
        operations = listOf(
            Operation(
                type = OrderType.BUY,
                asset = Instrument(
                    classCode = "adipisci",
                    figi = "deserunt",
                    first1dayCandleDate = "sapien",
                    first1minCandleDate = "vulputate",
                    forIisFlag = false,
                    instrumentType = "fermentum",
                    name = "Louisa Underwood",
                    ticker = "principes",
                    uid = "nisi",
                    price = 4.5
                ),
                price = 6.7,
                date = "indoctum",
                size = 2520,
                pnlValue = 12.13,
            ),
            Operation(
                type = OrderType.SELL,
                asset = Instrument(
                    classCode = "adipisci",
                    figi = "deserunt",
                    first1dayCandleDate = "sapien",
                    first1minCandleDate = "vulputate",
                    forIisFlag = false,
                    instrumentType = "fermentum",
                    name = "Louisa Underwood",
                    ticker = "principes",
                    uid = "nisi",
                    price = 4.5
                ),
                price = 6.7,
                date = "indoctum",
                size = 2520,
                pnlValue = 12.13,
            ),
            Operation(
                type = OrderType.BUY,
                asset = Instrument(
                    classCode = "adipisci",
                    figi = "deserunt",
                    first1dayCandleDate = "sapien",
                    first1minCandleDate = "vulputate",
                    forIisFlag = false,
                    instrumentType = "fermentum",
                    name = "Louisa Underwood",
                    ticker = "principes",
                    uid = "nisi",
                    price = 4.5
                ),
                price = 6.7,
                date = "indoctum",
                size = 2520,
                pnlValue = 12.13,
            ),
            Operation(
                type = OrderType.BUY,
                asset = Instrument(
                    classCode = "adipisci",
                    figi = "deserunt",
                    first1dayCandleDate = "sapien",
                    first1minCandleDate = "vulputate",
                    forIisFlag = false,
                    instrumentType = "fermentum",
                    name = "Louisa Underwood",
                    ticker = "principes",
                    uid = "nisi",
                    price = 4.5
                ),
                price = 6.7,
                date = "indoctum",
                size = 2520,
                pnlValue = 12.13,
            )
        ),
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


val instrumentsMock = listOf(
    Instrument(
        classCode = "AAPL",
        figi = "BBG000B9XRY4",
        first1dayCandleDate = "2000-01-01",
        first1minCandleDate = "2010-01-01",
        forIisFlag = true,
        instrumentType = "Equity",
        name = "Apple Inc.",
        ticker = "AAPL",
        uid = "uid1",
        price = 150.0
    ),
    Instrument(
        classCode = "T",
        figi = "BBG000B9XRY4",
        first1dayCandleDate = "2000-01-01",
        first1minCandleDate = "2010-01-01",
        forIisFlag = true,
        instrumentType = "Equity",
        name = "Т-Технологии",
        ticker = "T",
        uid = "uid1",
        price = 2371.0
    ),
    Instrument(
        classCode = "GOOGL",
        figi = "BBG009S39JX6",
        first1dayCandleDate = "2004-08-19",
        first1minCandleDate = "2014-08-19",
        forIisFlag = false,
        instrumentType = "Equity",
        name = "Alphabet Inc.",
        ticker = "GOOGL",
        uid = "uid2",
        price = 2800.0
    ),
    Instrument(
        classCode = "TSLA",
        figi = "BBG000N9MNX3",
        first1dayCandleDate = "2010-06-29",
        first1minCandleDate = "2015-06-29",
        forIisFlag = true,
        instrumentType = "Equity",
        name = "Tesla Inc.",
        ticker = "TSLA",
        uid = "uid3",
        price = 800.0
    ),
    Instrument(
        classCode = "MSFT",
        figi = "BBG000BPH459",
        first1dayCandleDate = "1986-03-13",
        first1minCandleDate = "1990-01-01",
        forIisFlag = false,
        instrumentType = "Equity",
        name = "Microsoft Corp.",
        ticker = "MSFT",
        uid = "uid4",
        price = 299.0
    ),
    Instrument(
        classCode = "AMZN",
        figi = "BBG000BVPV84",
        first1dayCandleDate = "1997-05-15",
        first1minCandleDate = "2000-05-15",
        forIisFlag = true,
        instrumentType = "Equity",
        name = "Amazon.com Inc.",
        ticker = "AMZN",
        uid = "uid5",
        price = 3300.0
    ),
    Instrument(
        classCode = "GAZP",
        figi = "BBG000BV8964",
        first1dayCandleDate = "1997-05-15",
        first1minCandleDate = "2000-05-15",
        forIisFlag = true,
        instrumentType = "Equity",
        name = "Газпром",
        ticker = "GAZP",
        uid = "uid6",
        price = 116.63
    ),
    Instrument(
        classCode = "SBER",
        figi = "BBG000F6Y8T3",
        first1dayCandleDate = "1997-05-15",
        first1minCandleDate = "2000-05-15",
        forIisFlag = true,
        instrumentType = "Equity",
        name = "Сбер Банк",
        ticker = "SBER",
        uid = "uid7",
        price = 233.48
    ),
    Instrument(
        classCode = "YDEX",
        figi = "TCSS09805522",
        first1dayCandleDate = "1997-05-15",
        first1minCandleDate = "2000-05-15",
        forIisFlag = true,
        instrumentType = "Equity",
        name = "Яндекс",
        ticker = "YDEX",
        uid = "uid8",
        price = 3378.5
    ),
)
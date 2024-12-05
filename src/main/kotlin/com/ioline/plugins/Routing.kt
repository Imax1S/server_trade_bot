package com.ioline.plugins

import com.ioline.common.sandboxToken
import com.ioline.common.toDouble
import com.ioline.data.models.Instrument
import com.ioline.database.BotRepository
import com.ioline.database.UserRepository
import com.ioline.routers.tradeBotRouting
import com.ioline.routers.userRouting
import com.ioline.strategies.StrategyFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tinkoff.piapi.contract.v1.InstrumentShort
import ru.tinkoff.piapi.core.InvestApi

fun Application.configureRouting() {
    val api = InvestApi.create(sandboxToken)
    val strategyFactory = StrategyFactory()
    val botRepository = BotRepository()
    val userRepository = UserRepository()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/findInstruments/{text?}") {
            val text = call.parameters["text"]
                ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)

            val foundInstruments = instrumentsMock.filter {
                it.ticker.contains(text, true) || it.figi.contains(text, true) || it.name.contains(text, true)
            }
            call.respond(foundInstruments)
        }

        get("/getPrice/{id?}") {
            val id = call.parameters["id"]
                ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)

            val price = api.marketDataService.getLastPricesSync(mutableListOf(id)).first().price.toDouble()

            call.respondText(price.toString())
        }
        tradeBotRouting(strategyFactory, botRepository)
        userRouting(userRepository)
    }
}

fun InstrumentShort.toInstrument(api: InvestApi): Instrument {
    val prices = api.marketDataService.getLastPricesSync(listOf(figi))
    return Instrument(
        classCode = classCode,
        figi = figi,
        first1dayCandleDate = first1DayCandleDate.toString(),
        first1minCandleDate = first1MinCandleDate.toString(),
        forIisFlag = forIisFlag,
        instrumentType = instrumentType,
        name = name,
        ticker = ticker,
        uid = uid,
        price = if (prices.isEmpty()) 0.0 else prices.last().price.toDouble()
    )
}

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



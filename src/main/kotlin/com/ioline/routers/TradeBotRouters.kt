package com.ioline.routers

import com.ioline.data.models.*
import com.ioline.database.BotRepository
import com.ioline.strategies.StrategyFactory
import com.ioline.strategies.TimeSettingsParser
import com.ioline.strategies.feeds.tinkoff_api.TinkoffFeedHistoricFeed
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.roboquant.Roboquant
import org.roboquant.brokers.sim.SimBroker
import org.roboquant.common.RUB
import org.roboquant.common.Wallet
import org.roboquant.metrics.AccountMetric
import ru.tinkoff.piapi.contract.v1.CandleInterval
import java.util.*

fun Route.tradeBotRouting(strategyFactory: StrategyFactory, botRepository: BotRepository) {
    route("/tradeBot") {
        getAllBots(botRepository)

        getBotById(botRepository)

        postCreateBot(botRepository)

        getRunBot(strategyFactory, botRepository)
    }
}

private fun Route.getRunBot(strategyFactory: StrategyFactory, botRepository: BotRepository) {
    post("/run/{id}") {
        val id = call.parameters["id"] ?: return@post call.respondText(
            "Missing id",
            status = HttpStatusCode.BadRequest
        )

        val tradeBot = botRepository.getTradeBotById(id)?.copy(
            instrumentsFIGI = listOf("TCSS09805522"),
            strategy = Strategy(
                type = StrategyType.EMA,
                param1 = "3",
                param2 = "10"
            ),
            timeSettings = TimeSettings(
                interval = CandleInterval.CANDLE_INTERVAL_DAY,
                start = "lacus",
                end = "inani",
                period = TimePeriod.YEARS
            )
        )
            ?: return@post call.respondText(
                "No bot with id=$id",
                status = HttpStatusCode.BadRequest
            )

        val type = tradeBot.strategy?.type
        val param1 = tradeBot.strategy?.param1?.toDoubleOrNull()
        val param2 = tradeBot.strategy?.param2?.toDoubleOrNull()
        val timeSettings = tradeBot.timeSettings

        if (param1 == null || param2 == null || type == null || timeSettings == null) {
            return@post call.respondText(
                "Error in parsing strategy params",
                status = HttpStatusCode.BadRequest
            )
        }

        val strategy = strategyFactory.createStrategy(
            type = type,
            param1 = param1,
            param2 = param2
        )

        val tinkoffFeed = TinkoffFeedHistoricFeed()
        val timeNumber = tradeBot.timeSettings.start.toIntOrNull() ?: 0
        val tradingPeriod = TimeSettingsParser.parse(timeSettings)

        tinkoffFeed.retrieve(
            tradeBot.instrumentsFIGI,
            interval = timeSettings.interval
        )

        val broker = SimBroker(
            initialDeposit = Wallet(1_000_000.RUB),
        )
//        val policy = FlexPolicy(orderPercentage = 1.0)

        val metric1 = AccountMetric()
        val bot = Roboquant(
            strategy = strategy,
            metric1,
//            policy = policy,
            broker = broker
        )
        bot.run(tinkoffFeed)

        val growthResult = bot.logger.getMetric("account.growth").map { it.value }

        val lastBalance = bot.logger.getMetric("account.equity").lastOrNull()
        val growth = bot.logger.getMetric("account.growth").lastOrNull()
        val operations = broker.account.trades.map { trade ->
            Operation(
                type = if (trade.size > 0) OrderType.BUY else OrderType.SELL,
                asset = trade.asset.toInstrument(),
                price = trade.totalCost.value,
                date = trade.time.toHttpDateString(),
                size = trade.size.toDouble().toInt(),
                pnlValue = trade.pnlValue
            )
        }

        val historicalResult = HistoricalResult(
            finalBalance = lastBalance?.value ?: 1000000.0,
            yield = growth?.value ?: 0.0,
            history = growthResult,
            operations = operations
        )
        call.respond(historicalResult)
    }
}

private fun Route.postCreateBot(botRepository: BotRepository) {
    post("/createBot") {
        var tradeBot: TradeBot? = null
        try {
            tradeBot = call.receive<TradeBot>()
        } catch (e: Exception) {
            println(e.message)
        }
        tradeBot ?: return@post call.respondText("Cannot create bot", status = HttpStatusCode.BadRequest)

        botRepository.createBot(tradeBot.copy(id = UUID.randomUUID().toString()))
        call.respond(tradeBot)
        call.respondText("Trade bot stored correctly", status = HttpStatusCode.Created)
    }
}

private fun Route.getBotById(botRepository: BotRepository) {
    get("bot/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            "Missing id",
            status = HttpStatusCode.BadRequest
        )

        val tradeBot = botRepository.getTradeBotById(id) ?: return@get call.respondText(
            "No trade bot with id $id",
            status = HttpStatusCode.NotFound
        )

        call.respond(tradeBot)
    }
}

private fun Route.getAllBots(botRepository: BotRepository) {
    get("/allBots") {
        val allBots = botRepository.getAllTradeBots()
        call.respond(allBots)
    }
}
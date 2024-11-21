package com.ioline.routers

import com.ioline.data.models.*
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
import org.roboquant.policies.FlexPolicy
import ru.tinkoff.piapi.core.InvestApi

fun Route.tradeBotRouting(api: InvestApi, strategyFactory: StrategyFactory) {
    route("/tradeBot") {
        getAllBots()

        getBotById()

        postCreateBot()

        getRunBot(strategyFactory)

        get("turnOff/{id}") {
//            val id = call.parameters["id"] ?: return@get call.respondText(
//                "Missing id",
//                status = HttpStatusCode.BadRequest
//            )
//            val strategy = strategiesStorage.find { it.tradeBot.id == id }
//            strategy?.tradeBot?.isActive = false
//
//            if (strategy == null) {
//                return@get call.respondText(
//                    "No bot with id",
//                    status = HttpStatusCode.BadRequest
//                )
//            }
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            if (tradeBotsStorage.removeIf { it.id == id }) {
                call.respondText("Trade bot removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}

private fun Route.getRunBot(strategyFactory: StrategyFactory) {
    post("/run/{id}") {
        val id = call.parameters["id"] ?: return@post call.respondText(
            "Missing id",
            status = HttpStatusCode.BadRequest
        )

        val tradeBot = tradeBotsStorage.find { it.id == id }
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
            initialDeposit = Wallet(100_000.RUB),
        )
        val policy = FlexPolicy(orderPercentage = 1.0)

        val metric1 = AccountMetric()
        val bot = Roboquant(
            strategy = strategy,
            metric1,
            policy = policy,
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

private fun Route.postCreateBot() {
    post("/createBot") {
        var tradeBot: TradeBot? = null
        try {
            tradeBot = call.receive<TradeBot>()
        } catch (e: Exception) {
            println(e.message)
        }
        tradeBot ?: return@post call.respondText("Cannot create bot", status = HttpStatusCode.BadRequest)

        tradeBotsStorage.add(tradeBot)
        call.respond(tradeBot)
        call.respondText("Trade bot stored correctly", status = HttpStatusCode.Created)
    }
}

private fun Route.getBotById() {
    get("bot/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            "Missing id",
            status = HttpStatusCode.BadRequest
        )

        val tradeBot =
            tradeBotsStorage.find { it.id == id } ?: return@get call.respondText(
                "No trade bot with id $id",
                status = HttpStatusCode.NotFound
            )

        call.respond(tradeBot)
    }
}

private fun Route.getAllBots() {
    get("/allBots") {
        if (tradeBotsStorage.isNotEmpty()) {
            call.respond(tradeBotsStorage)
        } else {
            call.respondText("No bots found", status = HttpStatusCode.OK)
        }
    }
}
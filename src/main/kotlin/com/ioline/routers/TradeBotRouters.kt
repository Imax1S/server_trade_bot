package com.ioline.routers

import com.ioline.data.models.HistoricalResult
import com.ioline.data.models.StrategyType
import com.ioline.data.models.TradeBot
import com.ioline.data.models.tradeBotsStorage
import com.ioline.strategies.feeds.tinkoff_api.TinkoffFeedHistoricFeed
import com.ioline.data.models.TimePeriod
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.roboquant.Roboquant
import org.roboquant.brokers.sim.SimBroker
import org.roboquant.common.*
import org.roboquant.metrics.AccountMetric
import org.roboquant.strategies.EMAStrategy
import org.roboquant.ta.RSIStrategy
import org.roboquant.ta.TaLibStrategy
import ru.tinkoff.piapi.core.InvestApi

fun Route.tradeBotRouting(api: InvestApi) {
    route("/tradeBot") {
        getAllBots()

        getBotById()

        postCreateBot()

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

            val strategy = when (tradeBot.strategy?.type) {
                StrategyType.EMA -> {
                    val fastPeriod = tradeBot.strategy.param1.toIntOrNull()
                    val slowPeriod = tradeBot.strategy.param2.toIntOrNull()

                    if (fastPeriod == null || slowPeriod == null) {
                        return@post call.respondText(
                            "Error in parsing strategy params",
                            status = HttpStatusCode.BadRequest
                        )
                    }
                    EMAStrategy(fastPeriod, slowPeriod)
                }
                StrategyType.RSI -> {
                    val lowThreshold = tradeBot.strategy.param1.toDoubleOrNull()
                    val highThreshold = tradeBot.strategy.param2.toDoubleOrNull()

                    if (lowThreshold == null || highThreshold == null) {
                        return@post call.respondText(
                            "Error in parsing strategy params",
                            status = HttpStatusCode.BadRequest
                        )
                    }
                    RSIStrategy(lowThreshold, highThreshold)
                }
                StrategyType.CUSTOM -> {
                    TaLibStrategy()
                }

                null -> TODO()
            }

            val tinkoffFeed = TinkoffFeedHistoricFeed()
            val timeNumber = tradeBot.timeSettings?.start?.toIntOrNull() ?: 0
            val tradingPeriod = when (tradeBot.timeSettings?.period) {
                TimePeriod.MINUTES -> timeNumber.minutes
                TimePeriod.HOURS -> timeNumber.hours
                TimePeriod.DAYS -> timeNumber.days
                TimePeriod.WEEKS -> timeNumber.weeks
                TimePeriod.MONTHS -> timeNumber.months
                TimePeriod.YEARS -> timeNumber.years
                null -> TODO()
            }
            tinkoffFeed.retrieve(
                tradeBot.instrumentsFIGI,
                timeframe = Timeframe.past(tradingPeriod),
                interval = tradeBot.timeSettings.interval
            )

            val broker = SimBroker(
                initialDeposit = Wallet(1_000_000.RUB)
            )

            val metric1 = AccountMetric()
            val bot = Roboquant(
                strategy, metric1, broker = broker
            )
            bot.run(tinkoffFeed)

            bot.logger.getMetric("account.growth").forEach {
                println(it.value.round(3))
            }

            bot.logger.getMetric("account.equity").forEach {
                println(it.value.round(3))
            }

            val lastBalance = bot.logger.getMetric("account.equity").lastOrNull()
            val growth = bot.logger.getMetric("account.growth").lastOrNull()

            val historicalResult = HistoricalResult(
                lastBalance?.value ?: 1000000.0,
                growth?.value ?: 0.0
            )
            call.respond(historicalResult)
        }

        get ("turnOff/{id}") {
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
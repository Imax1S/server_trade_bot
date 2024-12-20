package com.ioline.plugins

import com.ioline.common.sandboxToken
import com.ioline.common.toDouble
import com.ioline.data.models.instrumentsMock
import com.ioline.database.BotRepository
import com.ioline.database.UserRepository
import com.ioline.routers.tradeBotRouting
import com.ioline.routers.userRouting
import com.ioline.strategies.StrategyFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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

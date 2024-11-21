package com.ioline.plugins

import com.google.protobuf.util.JsonFormat
import com.ioline.common.sandboxToken
import com.ioline.common.toDouble
import com.ioline.routers.tradeBotRouting
import com.ioline.strategies.StrategyFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tinkoff.piapi.core.InvestApi

fun Application.configureRouting() {
    val api = InvestApi.create(sandboxToken)
    val strategyFactory = StrategyFactory()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/findInstrument/{id?}") {
            val id = call.parameters["id"]
                ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)

            val instruments = api.instrumentsService.findInstrumentSync(id).first()
            call.respond(JsonFormat.printer().print(instruments))
        }

        get("/getPrice/{id?}") {
            val id = call.parameters["id"]
                ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)

            val price = api.marketDataService.getLastPricesSync(mutableListOf(id)).first().price.toDouble()

            call.respondText(price.toString())
        }
        tradeBotRouting(api, strategyFactory)
    }
}




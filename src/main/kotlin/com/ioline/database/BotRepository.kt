package com.ioline.database

import com.ioline.data.models.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class BotRepository {
    fun createBot(tradeBot: TradeBot): String {
        return transaction {
            BotTable.insert { bot ->
                bot[id] = tradeBot.id
                bot[name] = tradeBot.name
                bot[description] = tradeBot.description
                bot[isActive] = tradeBot.isActive
                bot[marketEnvironment] = tradeBot.marketEnvironment.name
            }
        } get BotTable.id
    }

    fun getTradeBotById(botId: String): TradeBot? {
        return transaction {
            BotTable.select { BotTable.id eq botId }
                .map {
                    TradeBot(
                        id = it[BotTable.id],
                        name = it[BotTable.name],
                        description = it[BotTable.description],
                        isActive = it[BotTable.isActive],
                        marketEnvironment = MarketEnvironment.valueOf(it[BotTable.marketEnvironment]),
                        instrumentsFIGI = listOf("TCSS09805522"),
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
                    )
                }
        }.singleOrNull()
    }

    fun getAllTradeBots(): List<TradeBot> {
        return transaction {
            BotTable.selectAll().map {
                TradeBot(
                    id = it[BotTable.id],
                    name = it[BotTable.name],
                    description = it[BotTable.description],
                    isActive = it[BotTable.isActive],
                    marketEnvironment = MarketEnvironment.valueOf(it[BotTable.marketEnvironment]),
                )
            }
        }
    }

    fun updateTradeBotField(botId: String, isActive: Boolean): Boolean {
        return transaction {
            BotTable.update({ BotTable.id eq botId }) {
                it[BotTable.isActive] = isActive
            } > 0
        }
    }
}
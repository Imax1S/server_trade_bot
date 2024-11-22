package com.ioline.database

import com.ioline.data.models.MarketEnvironment
import com.ioline.data.models.TradeBot
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class Repository {
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
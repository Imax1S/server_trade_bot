package com.ioline.database

import org.jetbrains.exposed.sql.Table

const val MAX_VARCHAR_LENGTH = 128

object BotTable : Table("bots") {
    val id = varchar("id", 50)
    val name = varchar("name", MAX_VARCHAR_LENGTH)
    val description = varchar("description", MAX_VARCHAR_LENGTH)
    val isActive = bool("isActive").default(false)
    val marketEnvironment = varchar("market_environment", 50)

    override val primaryKey = PrimaryKey(id)
}
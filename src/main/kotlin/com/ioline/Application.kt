package com.ioline

import com.ioline.common.dataBaseConfig
import com.ioline.database.BotTable
import com.ioline.plugins.configureRouting
import com.ioline.plugins.configureSerialization
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    intiDatabase()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun intiDatabase() {
    val dataSource = HikariDataSource(dataBaseConfig)

    Database.connect(dataSource)

    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(BotTable)
    }
}

fun Application.module() {
    configureRouting()
    configureSerialization()
}

package com.ioline.common

import com.zaxxer.hikari.HikariConfig

val sandboxToken = ""

val dataBaseConfig = HikariConfig().apply {
    jdbcUrl = "jdbc:postgresql://localhost:5432/trade_bot"
    username = "postgres"
    password = "Cancan"
    maximumPoolSize = 10
}

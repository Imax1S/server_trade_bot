package com.ioline.database

import org.jetbrains.exposed.sql.Table

private const val MAX_VARCHAR_LENGTH = 128

object UserTable : Table("users") {
    val id = varchar("id", 50)
    val email = varchar("email", MAX_VARCHAR_LENGTH)
    val password = varchar("password", MAX_VARCHAR_LENGTH)
    val botsId = varchar("botsId", MAX_VARCHAR_LENGTH)

    override val primaryKey = PrimaryKey(id)
}
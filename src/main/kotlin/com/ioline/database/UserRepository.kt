package com.ioline.database

import com.ioline.data.models.User
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {
    fun createUser(user: User): String {
        return transaction {
            UserTable.insert { userTable ->
                userTable[id] = user.id
                userTable[email] = user.email
                userTable[password] = user.password
                userTable[botsId] = user.botsId
            }
        } get UserTable.id
    }

    fun getUser(userEmail: String, userPassword: String): User? {
        return transaction {
            UserTable.select {
                UserTable.email eq userEmail; UserTable.password eq userPassword
            }.map {
                User(
                    id = it[UserTable.id],
                    email = it[UserTable.email],
                    password = it[UserTable.password],
                    botsId = it[UserTable.botsId]
                )
            }
        }.singleOrNull()
    }
}
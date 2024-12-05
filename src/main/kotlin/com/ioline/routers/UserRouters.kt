package com.ioline.routers

import com.ioline.database.UserRepository
import io.ktor.server.routing.*

fun Route.userRouting(userRepository: UserRepository) {
    route("/user") {

    }
}
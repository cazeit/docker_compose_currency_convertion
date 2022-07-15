/*
 * Application
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner

import de.kba.api.wahrungsrechner.helper.ConvertionPathUtil
import de.kba.api.wahrungsrechner.model.PathSegment
import de.kba.api.wahrungsrechner.networking.helper.TRunner
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, 8080) {
        this.main()
    }
    server.start(true)
}

fun Application.main() {
    routing {
        route("/wr") {

            route("/health") {
                get {
                    TRunner.checkAndAwaitTRunner()
                    call.respond(HttpStatusCode.NoContent)
                }
            }

            route("/convertion") {
                get("/{fromCurrency}/{toCurrency}/{quantity?}") {
                    val response = ConvertionPathUtil.processCurrencyRequest(call.parameters, PathSegment.None)
                    val responseJson = response.data
                    val responseStatus = response.statusCode
                    call.respondText(responseJson, contentType = ContentType.Application.Json, status = responseStatus)
                }
                get("/{fromCurrency}/{toCurrency}/{quantity?}/debug/{debugParameter?}") {
                    val response = ConvertionPathUtil.processCurrencyRequest(call.parameters, PathSegment.Debug)
                    val responseJson = response.data
                    val responseStatus = response.statusCode
                    call.respondText(responseJson, contentType = ContentType.Application.Json, status = responseStatus)
                }
            }
        }
    }
}

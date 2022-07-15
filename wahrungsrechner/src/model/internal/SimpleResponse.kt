package de.kba.api.wahrungsrechner.model.internal

import io.ktor.http.*

class SimpleResponse(var data: String, var statusCode: HttpStatusCode)
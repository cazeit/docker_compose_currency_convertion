/*
 * CurrencyConvertionRequest
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.networking.requests

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.kba.api.wahrungsrechner.model.wahrungsrechner.CurrencyConvertionExternalApiResult
import de.kba.api.wahrungsrechner.networking.BaseRequest

class CurrencyConvertionRequest : BaseRequest() {

    var currencyFrom: String = ""
    var currencyTo: String = ""
    var quantity: Int = 1

    override val endpointUrlString: String
        get() {
            return "$currencyFrom/$currencyTo/json?quantity=$quantity"
        }

    override fun decode(data: String): Any {
        val mapper = ObjectMapper()

        return mapper.readValue<CurrencyConvertionExternalApiResult>(data)
    }
}
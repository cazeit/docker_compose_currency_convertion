/*
 * CurrencyConvertionInternalApiResult
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.model.internal

import com.fasterxml.jackson.annotation.JsonProperty

class CurrencyConvertionInternalApiResult {

    enum class ResultStatus(val description: String) {
        OK("OK"),
        ERROR("FAIL")
    }

    @JsonProperty("result")
    var result: CurrencyConvertionInternalResult? = null

    @JsonProperty("error")
    var error: CurrencyConvertionInternalError? = null

    @JsonProperty("status")
    var status: String = ResultStatus.ERROR.description

    @JsonProperty("source")
    val source: String = "Wahrungsrechner"
}
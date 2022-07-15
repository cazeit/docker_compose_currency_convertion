/*
 * CurrencyConvertionExternalApiResult
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.model.wahrungsrechner

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = false)
class CurrencyConvertionExternalApiResult {
    @JsonProperty("result")
    val result: CurrencyConvertionExternalResult? = null

    @JsonProperty("status")
    val status: String? = null
}
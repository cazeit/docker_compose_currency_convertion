/*
 * CurrencyConvertionInternalError
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.model.internal

import com.fasterxml.jackson.annotation.JsonProperty

class CurrencyConvertionInternalError() {
    @JsonProperty("code")
    var errorCode: Int? = null

    @JsonProperty("message")
    var errorMessage: String? = null
}
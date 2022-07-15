/*
 * PrecisionConvertionUtil
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.networking.helper

import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Util to define a decimal-precision used when calling the external API (wahrungsrechner only takes Int as quantity)
 */

object PrecisionConvertionUtil {

    private const val precision: Int = 2

    private val precisionFactor: Double
        get() {
            return (10.0).pow(precision)
        }

    fun convertForRequestWithPrecision(amount: Double): Int {
        val requestQuantity = amount * precisionFactor
        return requestQuantity.roundToInt()
    }

    fun convertResultWithPrecision(amount: Double): Double {
        return amount / precisionFactor
    }

}
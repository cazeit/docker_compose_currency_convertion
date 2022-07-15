/*
 * TRunner
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.networking.helper

import de.kba.api.wahrungsrechner.model.DebugParameter
import kotlin.random.Random

/**
 * Singleton that can make the service sleep so its not responding accoring to the passed debug parameter
 */
object TRunner {
    private const val maxSleepTime: Long = 10000
    private const val minSleepTime: Long = 1
    private const val defaultSleepTime: Long = 5000
    var isSleeping = false

    private fun sleepFor(sleepTime: Long) {
        isSleeping = true
        Thread.sleep(sleepTime)
        isSleeping = false
    }

    fun randomTime(): Long {
        return Random.nextLong(minSleepTime, maxSleepTime)
    }

    fun checkAndAwaitTRunner() {
        if (!isSleeping) {
            return
        }
        try {
            Thread.sleep(5000)
        } catch (e: Exception) {
        }
        checkAndAwaitTRunner()
    }

    fun sleep(debugParameter: DebugParameter) {
        when (debugParameter) {
            DebugParameter.fixed -> {
                sleepFor(defaultSleepTime)
            }
            DebugParameter.random -> {
                sleepFor(randomTime())
            }
            DebugParameter.noresponse -> {
                sleepFor(maxSleepTime * 10)
            }
        }
    }
}
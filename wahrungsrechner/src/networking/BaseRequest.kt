/*
 * BaseRequest
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.networking

import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import de.kba.api.wahrungsrechner.networking.helper.ApiResult
import de.kba.api.wahrungsrechner.networking.helper.NetworkError
import java.io.IOException

/**
 * BaseRequest for generic approach to easily add new requests to the API
 */
abstract class BaseRequest {
    companion object {
        const val apiKey = "4586|AVJs7_9Edb8OFFCNY1_zQAtRyjUKXTx2"
        const val baseUrlString = "https://api.wahrungsrechner.org/v1/quotes/"
        val client = OkHttpClient()
    }

    var onFinished: ((ApiResult<Any>) -> Unit)? = null

    private val requestUrlString: String
        get() {
            return "$baseUrlString$endpointUrlString&key=$apiKey"
        }

    protected abstract val endpointUrlString: String


    fun performRequest() {
        val request = Request.Builder().url(requestUrlString).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request?, exception: IOException?) {
                val error = NetworkError.offlineError()
                onFinished?.invoke(ApiResult.Error(error))
            }

            override fun onResponse(response: Response?) {
                val responseString = response?.body()?.string()
                if (responseString.isNullOrEmpty()) {
                    val error = NetworkError.decodeError()
                    onFinished?.invoke(ApiResult.Error(error))
                    return
                }
                try {
                    val result = decode(responseString)
                    onFinished?.invoke(ApiResult.Success(result))
                } catch (exception: Exception) {
                    val error = NetworkError.decodeError()
                    onFinished?.invoke(ApiResult.Error(error))
                }
            }

        })
    }

    abstract fun decode(data: String): Any
}
/*
 * MIT License
 *
 * Copyright (c) 2022 Sivan757
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package cn.kapukapu.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.slf4j.event.Level
import java.time.LocalDateTime

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        format {
            val httpInfo = getHttpDetail(it)

            HistoryInMemoryCache.put(httpInfo)
            httpInfo
        }
        val skipPathList = arrayOf("/favicon.ico", "/history")
        filter {
            val requestPath = it.request.path()
            skipPathList.forEach { path ->
                val startsWith = requestPath.startsWith(path)
                if (startsWith) {
                    return@filter false
                }
            }
            true
        }
    }
}

private fun getHttpDetail(it: ApplicationCall) = StringBuilder().apply {
    val requestURI = it.request.path()
    appendLine(it.request.origin.run { "${method.value} $scheme://$host:$port$requestURI $version" })

    appendLine("Date: ${LocalDateTime.now()}")

    it.request.headers.forEach { header, values ->
        appendLine("$header: ${values.firstOrNull()}")
    }

    it.request.queryParameters.forEach { header, values ->
        appendLine()
        append("$header: ${values.firstOrNull()}")
    }

    runBlocking {
        try {
            val value = it.request.receiveChannel().readUTF8Line()
            if(value?.isNotEmpty() == true){
                appendLine()
                appendLine(value)
            }
        } catch (_: RequestAlreadyConsumedException) {
        }
    }

}.toString()

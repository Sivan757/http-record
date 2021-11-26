package cn.kapukapu.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.slf4j.event.Level

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

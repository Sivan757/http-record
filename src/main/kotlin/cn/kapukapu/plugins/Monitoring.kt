package cn.kapukapu.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
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
        filter { !it.request.path().startsWith("/history") }
    }
}

private fun getHttpDetail(it: ApplicationCall) = StringBuilder().apply {
    appendLine("Received request:")
    val requestURI = it.request.path()
    appendLine(it.request.origin.run { "${method.value} $scheme://$host:$port$requestURI $version" })

    it.request.headers.forEach { header, values ->
        appendLine("$header: ${values.firstOrNull()}")
    }

    runBlocking {
        try {
            appendLine()
            appendLine(String(it.receive<ByteArray>()))
        } catch (_: RequestAlreadyConsumedException) {
        }
    }

}.toString()

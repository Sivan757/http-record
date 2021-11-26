package cn.kapukapu

import cn.kapukapu.plugins.configureHTTP
import cn.kapukapu.plugins.configureMonitoring
import cn.kapukapu.plugins.configureRouting
import cn.kapukapu.plugins.configureSerialization
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", watchPaths = listOf("classes")) {
        install(DoubleReceive)
        configureRouting()
        configureHTTP()
        configureSerialization()
        configureMonitoring()
    }.start(wait = true)
}

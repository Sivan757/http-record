package cn.kapukapu

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import cn.kapukapu.plugins.*
import io.ktor.application.*
import io.ktor.features.*
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        embeddedServer(Netty, port = 8080, host = "0.0.0.0", watchPaths = listOf("classes")) {
            install(DoubleReceive)
            configureRouting()
            configureHTTP()
            configureSerialization()
            configureMonitoring()
        }.start(wait = true)
    }
}

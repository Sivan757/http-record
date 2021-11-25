package cn.kapukapu.plugins

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(Logging)
}

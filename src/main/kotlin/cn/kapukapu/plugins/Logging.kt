package cn.kapukapu.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *
 *
 * @author Sivan
 */
class Logging(private val logger: Logger) {

    class Configuration {
        var logger: Logger = LoggerFactory.getLogger(Logging::class.java)
    }

    private suspend fun logRequest(call: ApplicationCall) {
        logger.info(StringBuilder().apply {
            appendLine("Received request:")
            val requestURI = call.request.path()
            appendLine(call.request.origin.run { "${method.value} $scheme://$host:$port$requestURI $version" })

            call.request.headers.forEach { header, values ->
                appendLine("$header: ${values.firstOrNull()}")
            }

            try {
                appendLine()
                appendLine(String(call.receive<ByteArray>()))
            } catch (e: RequestAlreadyConsumedException) {
                logger.error("Logging payloads requires DoubleReceive feature to be installed with receiveEntireContent=true", e)
            }
        }.toString())
    }

    private suspend fun logResponse(call: ApplicationCall, subject: Any) {
        logger.info(StringBuilder().apply {
            appendLine("Sent response:")
            appendLine("${call.request.httpVersion} ${call.response.status()}")
            call.response.headers.allValues().forEach { header, values ->
                appendLine("$header: ${values.firstOrNull()}")
            }
            when (subject) {
                is TextContent -> appendLine(subject.text)
                is OutputStreamContent -> appendLine() // ToDo: How to get response body??
                else -> appendLine("unknown body type")
            }
        }.toString())
    }

    /**
     * Feature installation.
     */
    fun install(pipeline: Application) {
        pipeline.intercept(ApplicationCallPipeline.Monitoring) {
            logRequest(call)
            proceedWith(subject)
        }
        pipeline.sendPipeline.addPhase(responseLoggingPhase)
        pipeline.sendPipeline.intercept(responseLoggingPhase) {
//            logResponse(call, subject)
        }
    }

    companion object Feature : ApplicationFeature<Application, Configuration, Logging> {

        override val key = AttributeKey<Logging>("Logging Feature")
        val responseLoggingPhase = PipelinePhase("ResponseLogging")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): Logging {
            val configuration = Configuration().apply(configure)

            return Logging(configuration.logger).apply { install(pipeline) }
        }
    }
}
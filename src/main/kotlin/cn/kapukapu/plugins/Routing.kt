package cn.kapukapu.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kotlinx.html.*
import kotlinx.html.ThScope.col

fun Application.configureRouting() {
    install(AutoHeadResponse)

    routing {
        get("/history") {
            call.respondHtml {
                head {
                    title {
                        +"Http-Record: 50 Record"
                    }
                    link {
                        href = "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
                        rel = "stylesheet"
                    }
                    script() {
                        src = "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
                    }
                }
                body {
                    h1("text-center") {
                        +"Recent 50 Record"
                    }
                    div("container") {
                        table("table table-bordered table-hover") {
                            thead("text-center") {
                                tr {
                                    th(scope = col) { +"#" }
                                    th(scope = col) { +"Request" }
                                }
                            }
                            tbody {
                                HistoryInMemoryCache.get().forEach {
                                    tr {
                                        td { pre { +it.first.toString() } }
                                        td { pre { +it.second } }
                                    }
                                }

                            }
                        }

                    }
                }
            }
        }
        get("/") {
            commonOutput()
        }
        get("/*") {
            echoOutput()
        }
        post("/*") {
            echoOutput()
        }
        put("/*") {
            echoOutput()
        }
        patch("/*") {
            echoOutput()
        }
        delete("/*") {
            echoOutput()
        }
        options("/*") {
            echoOutput()
        }
        head("/*") {
            echoOutput()
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.echoOutput() {
    val echo = call.request.queryParameters["echo"]
    val type = call.request.queryParameters["type"]
    when (type) {
        "text" -> call.respondText(contentType = ContentType.Text.Plain, text = echo ?: "")
        "json" -> call.respondText(contentType = ContentType.Application.Json, text = echo ?: "{}")
        else -> commonOutput()
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.commonOutput() {
    call.respondHtml {
        head {
            title {
                +"Http-Record"
            }

        }
        body {
            +"Hello, this is Http-Record."
        }
    }
}

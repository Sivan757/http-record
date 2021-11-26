package cn.kapukapu.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*

fun Application.configureRouting() {
    install(AutoHeadResponse)

    routing {
        get("/history") {
            call.respondHtml {
                head {
                    title = "Http-Record: 50 History"
                }
                body {
                    style = "display: flex;"
                    table {
                        tr {
                            td { +"index" }
                            td { +"content" }
                        }
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
        get("/") {
            call.respondText("Hello World!")
        }
        get("/*") {
            call.respondText("Hello World!")
        }
        post("/*") {
            call.respondText("Hello World!")
        }
    }
}

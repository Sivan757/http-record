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
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kotlinx.html.*
import kotlinx.html.ThScope.col

fun Application.configureRouting() {

    routing {
        historyPage()
        rootPage()
        notAllowWebSpider()
        recordApi()
    }
}

private fun Routing.notAllowWebSpider() {
    static {
        resource("robots.txt","robots.txt")
    }
}

private fun Routing.recordApi() {
    route("/{...}") {
        handle {
            echoOutput()
        }
    }
}

private fun Routing.rootPage() {
    get("/") {
        commonOutput()
    }
}

private fun Routing.historyPage() {
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

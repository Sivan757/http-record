package cn.kapukapu

import cn.kapukapu.plugins.configureRouting
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ configureRouting() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `test history page`() {
        withTestApplication({ configureRouting() }) {
            handleRequest(HttpMethod.Get, "/history").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}

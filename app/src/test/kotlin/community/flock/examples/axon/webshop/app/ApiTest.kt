package community.flock.examples.axon.webshop.app

import community.flock.examples.axon.webshop.app.environment.SpringBootTestWithContainers
import io.kotest.assertions.json.shouldBeValidJson
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.getForObject
import org.springframework.boot.resttestclient.postForObject
import java.time.Duration
import java.util.UUID

class ApiTest : SpringBootTestWithContainers() {
    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun testApi() {
        val id = restTemplate.postForObject<UUID>("/basket").shouldNotBeNull()
        val req =
            //language=json
            """{
                "title": "bla",
                "price": 5.00
            }"""
        restTemplate.postForObject<String>("/basket/$id/items", req) shouldBe "{}"
        restTemplate.postForObject<String>("/basket/$id/items", req) shouldBe "{}"
        val itemId = awaitItems(id) { size == 2 }.first()
        restTemplate.delete("/basket/$id/items/$itemId") shouldBe Unit
        awaitItems(id) { size == 1 }
        restTemplate.postForObject<String>("/basket/$id/items", req) shouldBe "{}"
        restTemplate.getForObject<String>("/basket") shouldContain "$id"
        restTemplate
            .getForObject<String>("/basket/$id/items")
            .shouldNotBeNull()
            .shouldBeValidJson()
    }

    @Test
    fun testRejectedCommand() {
        val id = restTemplate.postForObject<UUID>("/basket").shouldNotBeNull()
        val req =
            //language=json
            """{
                "title": "yolo",
                "price": 5.00
            }"""
        restTemplate.postForObject<String>("/basket/$id/items", req).shouldNotBeNull() shouldContain "Don't yolo!!!"
        awaitItems(id) { true }.shouldBeEmpty()
    }

    /** The projection catches up with the events in its own time, so wait until it shows what is expected. */
    private fun awaitItems(
        basketId: UUID,
        expected: List<String>.() -> Boolean,
    ): List<String> {
        fun items() = restTemplate.getForObject<List<Map<String, String>>>("/basket/$basketId/items").orEmpty().map { it.getValue("id") }
        await().atMost(Duration.ofSeconds(10)).until { items().expected() }
        return items()
    }
}

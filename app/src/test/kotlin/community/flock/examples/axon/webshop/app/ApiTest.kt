package community.flock.examples.axon.webshop.app

import community.flock.examples.axon.webshop.app.environment.SpringBootTestWithContainers
import io.kotest.assertions.json.shouldBeValidJson
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.getForObject
import org.springframework.boot.resttestclient.postForObject
import java.util.UUID

class ApiTest : SpringBootTestWithContainers() {
    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun testApi() {
        val id = restTemplate.postForObject<UUID>("/basket").shouldNotBeNull()
        println(id)
        val req =
            //language=json
            """{
                "title": "bla",
                "price": 5.00
            }"""
        restTemplate.postForObject<String>("/basket/$id/items", req) shouldBe "{}"
        restTemplate.postForObject<String>("/basket/$id/items", req) shouldBe "{}"
        restTemplate.delete("/basket/$id/items/0") shouldBe Unit
        restTemplate.postForObject<String>("/basket/$id/items", req) shouldBe "{}"
        restTemplate.getForObject<String>("/basket") shouldContain "$id"
        restTemplate
            .getForObject<String>("/basket/$id/items")
            .shouldNotBeNull()
            .shouldBeValidJson()
    }
}

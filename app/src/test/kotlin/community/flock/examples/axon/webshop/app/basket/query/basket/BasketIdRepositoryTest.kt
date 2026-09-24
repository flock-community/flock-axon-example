package community.flock.examples.axon.webshop.app.basket.query.basket

import community.flock.examples.axon.webshop.app.environment.SpringBootTestWithContainers
import community.flock.examples.axon.webshop.domain.shared.BasketId
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class BasketIdRepositoryTest : SpringBootTestWithContainers() {
    @Autowired
    private lateinit var repository: BasketIdRepository

    @Test
    fun testBasketRepository() {
        val basketId = BasketId()

        repository.addBasketId(basketId) shouldBe basketId
        repository.getBasketIds() shouldContain basketId
    }
}

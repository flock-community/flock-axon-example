package community.flock.examples.axon.webshop.app.basket.query.basket

import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class BasketRepositoryTest {
    @Test
    fun testBasketRepository() {
        val repository = BasketRepository()
        val basketId = BasketId()

        repository.addBasketId(basketId) shouldBe true
        repository.getBasketIds().first() shouldBe basketId
    }
}

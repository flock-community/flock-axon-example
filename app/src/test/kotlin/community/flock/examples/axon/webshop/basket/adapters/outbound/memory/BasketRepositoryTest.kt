package community.flock.examples.axon.webshop.basket.adapters.outbound.memory

import community.flock.examples.axon.webshop.basket.domain.model.BasketId
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

class BasketRepositoryTest {
    @Test
    fun testBasketRepository() {
        val repository = BasketRepository()
        val basketId = BasketId()

        repository.addBasketId(basketId)
        repository.getBasketIds().shouldContainExactly(basketId)
    }
}

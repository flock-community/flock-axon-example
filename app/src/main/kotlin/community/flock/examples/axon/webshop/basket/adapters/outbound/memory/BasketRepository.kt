package community.flock.examples.axon.webshop.basket.adapters.outbound.memory

import community.flock.examples.axon.webshop.basket.domain.model.BasketId
import community.flock.examples.axon.webshop.basket.domain.ports.ActiveBasketsPort
import org.springframework.stereotype.Repository

@Repository
class BasketRepository : ActiveBasketsPort {
    private val basketIds = mutableListOf<BasketId>()

    override fun addBasketId(basketId: BasketId) {
        basketIds.add(basketId)
    }

    override fun getBasketIds(): List<BasketId> = basketIds.toList()
}

package community.flock.examples.axon.webshop.app.basket.query.basket

import community.flock.examples.axon.webshop.domain.shared.BasketId
import org.springframework.stereotype.Repository

@Repository
class BasketRepository {
    private val basketIds = mutableListOf<BasketId>()

    fun addBasketId(basketId: BasketId) = basketIds.add(basketId)

    fun getBasketIds(): List<BasketId> = basketIds
}

package community.flock.examples.axon.webshop.basket.domain.ports

import community.flock.examples.axon.webshop.basket.domain.model.BasketId

/** The baskets that exist, as the read side keeps track of them. */
interface ActiveBasketsPort {
    fun addBasketId(basketId: BasketId)

    fun getBasketIds(): List<BasketId>
}

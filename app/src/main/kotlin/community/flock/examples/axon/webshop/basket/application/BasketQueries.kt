package community.flock.examples.axon.webshop.basket.application

import community.flock.examples.axon.webshop.basket.domain.model.BasketId

class GetAllActiveBasketIds

data class GetItemsQuery(
    val basketId: BasketId,
)

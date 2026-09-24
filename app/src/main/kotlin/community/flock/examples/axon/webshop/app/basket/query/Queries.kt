package community.flock.examples.axon.webshop.app.basket.query

import community.flock.examples.axon.webshop.domain.shared.BasketId

class GetAllActiveBasketIds

data class GetItemsQuery(
    val basketId: BasketId,
)

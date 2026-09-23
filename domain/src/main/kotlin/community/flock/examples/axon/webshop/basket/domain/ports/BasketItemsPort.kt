package community.flock.examples.axon.webshop.basket.domain.ports

import community.flock.examples.axon.webshop.basket.domain.model.BasketId
import community.flock.examples.axon.webshop.basket.domain.model.Item
import community.flock.examples.axon.webshop.basket.domain.model.ItemId

/** The items of every basket, as the read side stores them. */
interface BasketItemsPort {
    fun createBasket(basketId: BasketId): BasketId

    fun getAllItemsFromBasket(basketId: BasketId): List<Item>

    fun saveItemInBasket(
        basketId: BasketId,
        item: Item,
    ): Item

    fun deleteItemFromBasketById(
        basketId: BasketId,
        itemId: ItemId,
    ): Item?
}

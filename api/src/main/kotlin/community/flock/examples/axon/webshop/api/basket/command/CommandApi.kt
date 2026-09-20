package community.flock.examples.axon.webshop.api.basket.command

import community.flock.examples.axon.webshop.api.basket.command.model.ItemDto
import java.util.UUID

interface CommandApi :
    BasketCommandApi,
    ItemCommandApi

interface BasketCommandApi {
    suspend fun getNewBasket(): UUID
}

interface ItemCommandApi {
    suspend fun postItem(
        potentialBasketId: String,
        potentialItem: ItemDto,
    ): ItemDto?

    suspend fun deleteItem(
        potentialBasketId: String,
        potentialItemId: String,
    ): ItemDto?
}

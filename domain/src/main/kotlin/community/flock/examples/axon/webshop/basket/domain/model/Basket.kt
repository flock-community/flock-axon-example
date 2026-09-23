package community.flock.examples.axon.webshop.basket.domain.model

/**
 * A basket and the items in it. Immutable: adding and removing yield the next state, so the
 * command model can both decide on a command and rebuild the basket from its history with it.
 */
data class Basket(
    val id: BasketId,
    val items: Map<ItemId, Item> = emptyMap(),
) {
    val totalPrice: Price
        get() = items.values.fold(Price.ZERO) { total, item -> total + item.price }

    /** The basket with [item] in it. Rejects an item that breaks the shop's one rule. */
    fun add(item: Item): Basket {
        require(!item.isYolo) { "Don't yolo!!!" }
        return copy(items = items + (item.id to item))
    }

    /** The basket without the item with [itemId]; unchanged when there is no such item. */
    fun remove(itemId: ItemId): Basket = copy(items = items - itemId)
}

private val Item.isYolo: Boolean
    get() = title.value.lowercase() == "yolo"

package community.flock.examples.axon.webshop.basket.adapters.outbound.database

import community.flock.examples.axon.webshop.basket.adapters.outbound.database.mappers.ItemConverter.externalize
import community.flock.examples.axon.webshop.basket.adapters.outbound.database.mappers.ItemConverter.internalize
import community.flock.examples.axon.webshop.basket.adapters.outbound.database.model.BasketEntity
import community.flock.examples.axon.webshop.basket.domain.model.BasketId
import community.flock.examples.axon.webshop.basket.domain.model.Item
import community.flock.examples.axon.webshop.basket.domain.model.ItemId
import community.flock.examples.axon.webshop.basket.domain.ports.BasketItemsPort
import org.springframework.stereotype.Repository

@Repository
class ItemRepository(
    private val repo: PostgresRepository,
) : BasketItemsPort {
    override fun createBasket(basketId: BasketId): BasketId =
        BasketEntity(id = basketId.value)
            .let { repo.save(it) }
            .id
            .let(::BasketId)

    override fun getAllItemsFromBasket(basketId: BasketId): List<Item> =
        getBasketById(basketId)
            ?.items
            .orEmpty()
            .map { it.internalize() }

    override fun saveItemInBasket(
        basketId: BasketId,
        item: Item,
    ): Item =
        run {
            val (id) = basketId
            val newItems = getAllItemsFromBasket(basketId) + item
            BasketEntity(id = id, items = newItems.map { it.externalize() })
                .let(repo::save)
            item
        }

    override fun deleteItemFromBasketById(
        basketId: BasketId,
        itemId: ItemId,
    ): Item? =
        run {
            val (id) = basketId
            val items = getAllItemsFromBasket(basketId)
            val item = items.find { it.id == itemId }
            val newBasket = BasketEntity(id = id, items = items.filterNot { it.id == itemId }.map { it.externalize() })
            repo.save(newBasket)
            item
        }

    private fun getBasketById(basketId: BasketId): BasketEntity? =
        run {
            val (id) = basketId
            repo.findById(id).orElse(null)
        }
}

package community.flock.examples.axon.webshop.app.basket.query.item

import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.query.item.ItemTransformer.externalize
import community.flock.examples.axon.webshop.app.basket.query.item.ItemTransformer.internalize
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Document
data class BasketEntity(
    @Id
    val id: UUID,
    val items: List<ItemEntity> = emptyList(),
)

data class ItemEntity(
    val id: Int,
    val title: String,
    val price: String,
)

@Repository
class ItemRepository(
    private val repo: MongoDBRepository,
) {
    fun createBasket(basketId: BasketId): BasketId =
        runBlocking {
            BasketEntity(id = basketId.value)
                .let { repo.save(it) }
                .awaitSingle()
                .id
                .let(::BasketId)
        }

    fun getAllItemsFromBasket(basketId: BasketId): List<Item> =
        getBasketById(basketId)
            ?.items
            .orEmpty()
            .map { it.internalize() }

    fun saveItemInBasket(
        basketId: BasketId,
        item: Item,
    ): Item =
        runBlocking {
            val (id) = basketId
            val newItems = getAllItemsFromBasket(basketId) + item
            BasketEntity(id = id, items = newItems.map { it.externalize() })
                .let { repo.save(it) }
                .awaitSingle()
            item
        }

    fun deleteItemFromBasketById(
        basketId: BasketId,
        itemId: ItemId,
    ): Item? =
        runBlocking {
            val (id) = basketId
            val items = getAllItemsFromBasket(basketId)
            val item = items.find { it.id == itemId }
            val newBasket = BasketEntity(id = id, items = items.filterNot { it.id == itemId }.map { it.externalize() })
            repo.save(newBasket).awaitSingle()
            item
        }

    private fun getBasketById(basketId: BasketId): BasketEntity? =
        runBlocking {
            val (id) = basketId
            repo.findById(id).awaitSingleOrNull()
        }
}

interface MongoDBRepository : ReactiveCrudRepository<BasketEntity, UUID>

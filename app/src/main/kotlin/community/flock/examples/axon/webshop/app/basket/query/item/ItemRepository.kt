package community.flock.examples.axon.webshop.app.basket.query.item

import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.query.item.ItemTransformer.externalize
import community.flock.examples.axon.webshop.app.basket.query.item.ItemTransformer.internalize
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Entity
data class BasketEntity(
    @Id
    val id: UUID,
    @ElementCollection(fetch = FetchType.EAGER)
    val items: List<ItemEntity> = emptyList(),
)

@Embeddable
data class ItemEntity(
    val id: Int,
    val title: String,
    val price: String,
)

@Repository
class ItemRepository(
    private val repo: PostgresRepository,
) {
    fun createBasket(basketId: BasketId): BasketId =
        BasketEntity(id = basketId.value)
            .let { repo.save(it) }
            .id
            .let(::BasketId)

    fun getAllItemsFromBasket(basketId: BasketId): List<Item> =
        getBasketById(basketId)
            ?.items
            .orEmpty()
            .map { it.internalize() }

    fun saveItemInBasket(
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

    fun deleteItemFromBasketById(
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

interface PostgresRepository : JpaRepository<BasketEntity, UUID>

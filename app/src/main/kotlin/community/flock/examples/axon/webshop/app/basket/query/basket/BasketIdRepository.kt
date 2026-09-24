package community.flock.examples.axon.webshop.app.basket.query.basket

import community.flock.examples.axon.webshop.domain.shared.BasketId
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Entity
data class BasketIdsEntity(
    @Id
    val id: Int = 1,
    val basketIds: MutableSet<UUID> = mutableSetOf(),
)

@Repository
class BasketIdRepository(
    private val repo: BasketIdsPostgresRepository,
) {
    fun addBasketId(basketId: BasketId) =
        basketId.also {
            repo
                .findById(1)
                .orElseGet { BasketIdsEntity() }
                .apply { basketIds.add(it.value) }
                .also(repo::save)
        }

    fun getBasketIds(): List<BasketId> =
        repo
            .findById(1)
            .orElseGet { BasketIdsEntity() }
            .basketIds
            .map(::BasketId)
}

interface BasketIdsPostgresRepository : JpaRepository<BasketIdsEntity, Int>

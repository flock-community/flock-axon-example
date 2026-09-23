package community.flock.examples.axon.webshop.basket.adapters.outbound.database.model

import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
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

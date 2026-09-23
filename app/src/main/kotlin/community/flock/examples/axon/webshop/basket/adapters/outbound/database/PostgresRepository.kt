package community.flock.examples.axon.webshop.basket.adapters.outbound.database

import community.flock.examples.axon.webshop.basket.adapters.outbound.database.model.BasketEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PostgresRepository : JpaRepository<BasketEntity, UUID>

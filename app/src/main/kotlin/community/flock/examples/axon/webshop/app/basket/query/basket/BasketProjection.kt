package community.flock.examples.axon.webshop.app.basket.query.basket

import community.flock.examples.axon.webshop.app.basket.query.GetAllActiveBasketIds
import community.flock.examples.axon.webshop.domain.event.BasketCreatedEvent
import community.flock.examples.axon.webshop.domain.shared.BasketId
import org.axonframework.messaging.eventhandling.annotation.EventHandler
import org.axonframework.messaging.queryhandling.annotation.QueryHandler
import org.springframework.stereotype.Component

@Component
class BasketProjection(
    private val repository: BasketIdRepository,
) {
    @QueryHandler
    fun handleQuery(query: GetAllActiveBasketIds): List<BasketId> = repository.getBasketIds()

    @EventHandler
    fun on(event: BasketCreatedEvent) {
        repository.addBasketId(event.basketId)
    }
}

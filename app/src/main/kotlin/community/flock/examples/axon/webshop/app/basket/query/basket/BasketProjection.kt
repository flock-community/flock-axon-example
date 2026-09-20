package community.flock.examples.axon.webshop.app.basket.query.basket

import community.flock.examples.axon.webshop.app.basket.query.GetAllActiveBasketIds
import community.flock.examples.axon.webshop.app.basket.shared.BasketCreatedEvent
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class BasketProjection(
    private val repository: BasketRepository,
) {
    @QueryHandler
    fun handleQuery(query: GetAllActiveBasketIds): List<BasketId> = repository.getBasketIds()

    @EventHandler
    fun on(event: BasketCreatedEvent) {
        repository.addBasketId(event.basketId)
    }
}

package community.flock.examples.axon.webshop.basket.application

import community.flock.examples.axon.webshop.basket.domain.model.BasketId
import community.flock.examples.axon.webshop.basket.domain.ports.ActiveBasketsPort
import org.axonframework.messaging.eventhandling.annotation.EventHandler
import org.axonframework.messaging.queryhandling.annotation.QueryHandler
import org.springframework.stereotype.Component

@Component
class BasketProjection(
    private val activeBaskets: ActiveBasketsPort,
) {
    @QueryHandler
    fun handleQuery(query: GetAllActiveBasketIds): List<BasketId> = activeBaskets.getBasketIds()

    @EventHandler
    fun on(event: BasketCreatedEvent) {
        activeBaskets.addBasketId(event.basketId)
    }
}

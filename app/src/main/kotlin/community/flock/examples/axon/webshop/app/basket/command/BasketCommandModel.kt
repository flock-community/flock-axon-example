package community.flock.examples.axon.webshop.app.basket.command

import community.flock.examples.axon.webshop.app.common.axon.DeciderEntityModule
import community.flock.examples.axon.webshop.app.common.axon.asEventSourcedEntity
import community.flock.examples.axon.webshop.domain.basket.Basket
import community.flock.examples.axon.webshop.domain.basket.BasketCommand
import community.flock.examples.axon.webshop.domain.basket.basketDecider
import community.flock.examples.axon.webshop.domain.event.BasketEvent
import community.flock.examples.axon.webshop.domain.shared.BasketId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

const val BASKET_TAG = "basketId"

/** The basket decider, run by Axon: its events are tagged with the basket id, which is also what routes its commands. */
@Configuration
class BasketCommandModel {
    @Bean
    fun basketEntity(): DeciderEntityModule<BasketId, Basket> =
        basketDecider.asEventSourcedEntity(
            tagKey = BASKET_TAG,
            commandId = BasketCommand::basketId,
            eventId = BasketEvent::basketId,
        )
}

package community.flock.examples.axon.webshop.app.basket.command

import community.flock.examples.axon.webshop.api.model.UUID
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.common.Producer

object BasketIdProducer : Producer<BasketId, UUID> {
    override fun BasketId.produce(): UUID = UUID(toString())
}

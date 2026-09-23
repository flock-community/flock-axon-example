package community.flock.examples.axon.webshop.basket.adapters.inbound.controllers

import community.flock.examples.axon.webshop.api.model.UUID
import community.flock.examples.axon.webshop.basket.domain.model.BasketId
import community.flock.examples.axon.webshop.common.Producer

object BasketIdProducer : Producer<BasketId, UUID> {
    override fun BasketId.produce(): UUID = UUID(toString())
}

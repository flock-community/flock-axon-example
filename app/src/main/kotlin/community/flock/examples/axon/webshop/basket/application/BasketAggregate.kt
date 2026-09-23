package community.flock.examples.axon.webshop.basket.application

import community.flock.examples.axon.webshop.basket.domain.model.Basket
import community.flock.examples.axon.webshop.basket.domain.model.BasketId
import org.axonframework.eventsourcing.annotation.EventSourcedEntity
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender

/**
 * The command model of a basket: Axon sources it from the basket's events and routes the basket's
 * commands to it. It keeps a [Basket] of the domain, asks it whether a command is acceptable and
 * records the outcome as an event, which then moves the basket to its next state.
 */
@EventSourced(idType = BasketId::class)
@EventSourcedEntity(tagKey = "basketId")
class BasketAggregate
    @EntityCreator
    constructor() {
        private var basket: Basket? = null

        init {
            println("Aggregate constructed")
        }

        @CommandHandler
        fun createBasket(
            command: CreateBasketCommand,
            eventAppender: EventAppender,
        ) {
            println("Create Basket with id: ${command.basketId}")
            eventAppender.append(BasketCreatedEvent(command.basketId))
        }

        @EventSourcingHandler
        fun on(event: BasketCreatedEvent) {
            println("Basket Created with id: ${event.basketId}")
            basket = Basket(event.basketId)
        }

        @CommandHandler
        fun addItem(
            command: AddItemCommand,
            eventAppender: EventAppender,
        ) {
            val (basketId, item) = command
            basket(basketId).add(item)
            println("Adding Item for basket: $basketId with itemId: ${item.id}")
            eventAppender.append(ItemAddedEvent(basketId, item))
        }

        @EventSourcingHandler
        fun on(event: ItemAddedEvent) {
            val (basketId, item) = event
            basket = basket(basketId).add(item)
            println("Item Added to basket: $basketId with itemId: ${item.id}")
        }

        @CommandHandler
        fun removeItem(
            command: RemoveItemCommand,
            eventAppender: EventAppender,
        ) {
            val (basketId, itemId) = command
            basket(basketId).remove(itemId)
            println("Removing Item")
            eventAppender.append(ItemRemovedEvent(basketId, itemId))
        }

        @EventSourcingHandler
        fun on(event: ItemRemovedEvent) {
            val (basketId, itemId) = event
            println("Item Removed")
            basket = basket(basketId).remove(itemId)
        }

        private fun basket(basketId: BasketId): Basket = checkNotNull(basket) { "Basket $basketId does not exist" }
    }

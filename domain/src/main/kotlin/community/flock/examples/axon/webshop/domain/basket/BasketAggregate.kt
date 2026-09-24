package community.flock.examples.axon.webshop.domain.basket

import community.flock.examples.axon.webshop.domain.event.BasketCreatedEvent
import community.flock.examples.axon.webshop.domain.event.BasketEvent.Companion.BASKET_TAG
import community.flock.examples.axon.webshop.domain.event.ItemAddedEvent
import community.flock.examples.axon.webshop.domain.event.ItemRemovedEvent
import community.flock.examples.axon.webshop.domain.shared.BasketId
import community.flock.examples.axon.webshop.domain.shared.ItemId
import org.axonframework.eventsourcing.annotation.EventSourcedEntity
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender

@EventSourced(idType = BasketId::class)
@EventSourcedEntity(tagKey = BASKET_TAG)
class BasketAggregate
    @EntityCreator
    constructor() {
        private lateinit var basketId: BasketId
        private var items: MutableMap<ItemId, Item> = mutableMapOf()
        private var totalPrice: Price = Price(0.00)

        init {
            println("Aggregate constructed")
        }

        companion object {
            @JvmStatic
            @CommandHandler
            fun createBasket(
                command: CreateBasketCommand,
                eventAppender: EventAppender,
            ): BasketId =
                command.basketId
                    .also { println("Create Basket with id: $it") }
                    .also { eventAppender.append(BasketCreatedEvent(it)) }
        }

        @EventSourcingHandler
        fun on(event: BasketCreatedEvent) {
            println("Basket Created with id: ${event.basketId}")
            basketId = event.basketId
        }

        @CommandHandler
        fun addItem(
            command: AddItemCommand,
            eventAppender: EventAppender,
        ): BasketId =
            command
                .also { (_, item) -> if (item.title.value.lowercase() == "yolo") throw RuntimeException("Don't yolo!!!") }
                .also { (basketId, item) -> println("Adding Item for basket: $basketId with itemId: ${item.id}") }
                .also { (basketId, item) -> eventAppender.append(ItemAddedEvent(basketId, item)) }
                .basketId

        @EventSourcingHandler
        fun on(event: ItemAddedEvent): Unit =
            event.let { (basketId, item) ->
                val itemId = item.id
                items[itemId] = item
                totalPrice = items.totalPrice()
                println("Item Added to basket: $basketId with itemId: $itemId")
            }

        @CommandHandler
        fun removeItem(
            command: RemoveItemCommand,
            eventAppender: EventAppender,
        ): BasketId =
            command
                .also { println("Removing Item") }
                .also { (basketId, itemId) -> eventAppender.append(ItemRemovedEvent(basketId, itemId)) }
                .basketId

        @EventSourcingHandler
        fun on(event: ItemRemovedEvent) {
            println("Item Removed")
            items.remove(event.itemId)
            totalPrice = items.totalPrice()
        }

        private fun Map<ItemId, Item>.totalPrice() = values.fold(Price(0.00)) { acc, cur -> acc + cur.price }
    }

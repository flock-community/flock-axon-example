package community.flock.examples.axon.webshop.app.basket.command.model

import community.flock.examples.axon.webshop.app.basket.command.AddItemCommand
import community.flock.examples.axon.webshop.app.basket.command.CreateBasketCommand
import community.flock.examples.axon.webshop.app.basket.command.RemoveItemCommand
import community.flock.examples.axon.webshop.app.basket.event.BasketCreatedEvent
import community.flock.examples.axon.webshop.app.basket.event.ItemAddedEvent
import community.flock.examples.axon.webshop.app.basket.event.ItemRemovedEvent
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import org.axonframework.eventsourcing.annotation.EventSourcedEntity
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender

@EventSourced(idType = BasketId::class)
@EventSourcedEntity(tagKey = "basketId")
class BasketAggregate
    @EntityCreator
    constructor() {
        private lateinit var basketId: BasketId

        private var items: MutableMap<ItemId, Item>

        private var totalPrice: Price = Price(0.00)

        init {
            items = mutableMapOf()
            println("Aggregate constructed")
        }

        @CommandHandler
        fun createBasket(
            command: CreateBasketCommand,
            eventAppender: EventAppender,
        ): Unit =
            command.let { command ->
                println("Create Basket with id: ${command.basketId}")
                eventAppender.append(BasketCreatedEvent(command.basketId))
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
        ): Unit =
            command.let { (basketId, item) ->
                val itemId = item.id
                if (item.title.value.lowercase() == "yolo") throw RuntimeException("Don't yolo!!!")
                println("Adding Item for basket: $basketId with itemId: $itemId")
                eventAppender.append(ItemAddedEvent(basketId, item))
            }

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
        ) {
            println("Removing Item")
            eventAppender.append(ItemRemovedEvent(command.basketId, command.itemId))
        }

        @EventSourcingHandler
        fun on(event: ItemRemovedEvent) {
            println("Item Removed")
            items.remove(event.itemId)
            totalPrice = items.totalPrice()
        }

        private fun Map<ItemId, Item>.totalPrice() = values.fold(Price(0.00)) { acc, cur -> acc + cur.price }
    }

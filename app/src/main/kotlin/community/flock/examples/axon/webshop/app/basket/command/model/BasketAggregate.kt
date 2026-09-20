package community.flock.examples.axon.webshop.app.basket.command.model

import community.flock.examples.axon.webshop.app.basket.command.AddItemCommand
import community.flock.examples.axon.webshop.app.basket.command.CreateBasketCommand
import community.flock.examples.axon.webshop.app.basket.command.RemoveItemCommand
import community.flock.examples.axon.webshop.app.basket.shared.BasketCreatedEvent
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemAddedEvent
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import community.flock.examples.axon.webshop.app.basket.shared.ItemRemovedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.modelling.command.AggregateMember
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class BasketAggregate() {
    @AggregateIdentifier
    private lateinit var basketId: BasketId

    @AggregateMember
    private val items = mutableMapOf<ItemId, Item>()

    private var totalPrice: Price = Price(0.00)

    init {
        println("Aggregate constructed")
    }

    @CommandHandler
    constructor(command: CreateBasketCommand) : this() {
        println("Create Basket with id: ${command.basketId}")
        apply(BasketCreatedEvent(command.basketId))
    }

    @EventSourcingHandler
    fun on(event: BasketCreatedEvent) {
        println("Basket Created with id: ${event.basketId}")
        basketId = event.basketId
    }

    @CommandHandler
    fun addItem(command: AddItemCommand): Unit =
        command.let { (basketId, item) ->
            val itemId = item.id
            if (item.title.value.lowercase() == "yolo") throw RuntimeException("Don't yolo!!!")
            println("Adding Item for basket: $basketId with itemId: $itemId")
            apply(ItemAddedEvent(basketId, item))
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
    fun removeItem(command: RemoveItemCommand) {
        println("Removing Item")
        apply(ItemRemovedEvent(command.basketId, command.itemId))
    }

    @EventSourcingHandler
    fun on(event: ItemRemovedEvent) {
        println("Item Removed")
        items.remove(event.itemId)
        totalPrice = items.totalPrice()
    }

    private fun Map<ItemId, Item>.totalPrice() = values.fold(Price(0.00)) { acc, cur -> acc + cur.price }
}

package community.flock.examples.axon.webshop.app.basket.command

import community.flock.examples.axon.webshop.app.basket.command.model.BasketAggregate
import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.command.model.Price
import community.flock.examples.axon.webshop.app.basket.command.model.Title
import community.flock.examples.axon.webshop.app.basket.event.BasketCreatedEvent
import community.flock.examples.axon.webshop.app.basket.event.ItemAddedEvent
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.Test

class BasketAggregateTest {
    private val testFixture = AggregateTestFixture(BasketAggregate::class.java)

    @Test
    fun testBasketAggregate() {
        val basketId = BasketId()
        val itemId = ItemId()
        val item = Item(id = itemId, title = Title("content one"), price = Price(1.00))

        testFixture
            .given(BasketCreatedEvent(basketId))
            .`when`(AddItemCommand(basketId, item))
            .expectSuccessfulHandlerExecution()
            .expectEvents(ItemAddedEvent(basketId, item))
    }
}

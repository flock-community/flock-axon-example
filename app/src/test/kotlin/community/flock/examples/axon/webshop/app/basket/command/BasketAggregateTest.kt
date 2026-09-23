package community.flock.examples.axon.webshop.app.basket.command

import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.command.model.Price
import community.flock.examples.axon.webshop.app.basket.command.model.Title
import community.flock.examples.axon.webshop.app.basket.event.BasketCreatedEvent
import community.flock.examples.axon.webshop.app.basket.event.ItemAddedEvent
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import community.flock.examples.axon.webshop.app.environment.SpringBootTestWithContainers
import org.axonframework.extension.spring.config.SpringAxonApplication
import org.axonframework.test.fixture.AxonTestFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class BasketAggregateTest : SpringBootTestWithContainers() {
    private lateinit var testFixture: AxonTestFixture

    @Autowired
    private lateinit var applicationConfigurer: SpringAxonApplication

    @BeforeEach
    fun beforeEach() {
        testFixture = AxonTestFixture.with(applicationConfigurer)
    }

    @AfterEach
    fun afterEach() {
        testFixture.stop()
    }

    @Test
    fun testBasketAggregate() {
        val basketId = BasketId()
        val itemId = ItemId()
        val item = Item(id = itemId, title = Title("content one"), price = Price(1.00))

        testFixture
            .given()
            .event(BasketCreatedEvent(basketId))
            .`when`()
            .command(AddItemCommand(basketId, item))
            .then()
            .success()
            .events(ItemAddedEvent(basketId, item))
    }
}

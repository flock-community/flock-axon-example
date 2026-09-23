package community.flock.examples.axon.webshop.basket.application

import community.flock.examples.axon.webshop.basket.domain.model.BasketId
import community.flock.examples.axon.webshop.basket.domain.model.Item
import community.flock.examples.axon.webshop.basket.domain.model.ItemId
import community.flock.examples.axon.webshop.basket.domain.model.Price
import community.flock.examples.axon.webshop.basket.domain.model.Title
import community.flock.examples.axon.webshop.environment.SpringBootTestWithContainers
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

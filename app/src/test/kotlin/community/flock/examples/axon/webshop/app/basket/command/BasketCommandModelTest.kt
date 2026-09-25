package community.flock.examples.axon.webshop.app.basket.command

import community.flock.examples.axon.webshop.app.environment.SpringBootTestWithContainers
import community.flock.examples.axon.webshop.domain.basket.AddItemCommand
import community.flock.examples.axon.webshop.domain.basket.CreateBasketCommand
import community.flock.examples.axon.webshop.domain.basket.Item
import community.flock.examples.axon.webshop.domain.basket.Price
import community.flock.examples.axon.webshop.domain.basket.RemoveItemCommand
import community.flock.examples.axon.webshop.domain.basket.Title
import community.flock.examples.axon.webshop.domain.event.BasketCreatedEvent
import community.flock.examples.axon.webshop.domain.event.ItemAddedEvent
import community.flock.examples.axon.webshop.domain.event.ItemRemovedEvent
import community.flock.examples.axon.webshop.domain.shared.BasketId
import community.flock.examples.axon.webshop.domain.shared.DomainException
import community.flock.examples.axon.webshop.domain.shared.ItemId
import org.axonframework.extension.spring.config.SpringAxonApplication
import org.axonframework.test.fixture.AxonTestFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

/** The decider as Axon runs it: events are loaded by the basket tag, folded into state, and the decision appended. */
class BasketCommandModelTest : SpringBootTestWithContainers() {
    private lateinit var testFixture: AxonTestFixture

    @Autowired
    private lateinit var applicationConfigurer: SpringAxonApplication

    private val basketId = BasketId()
    private val item = Item(id = ItemId(), title = Title("content one"), price = Price(1.00))

    @BeforeEach
    fun beforeEach() {
        testFixture = AxonTestFixture.with(applicationConfigurer)
    }

    @AfterEach
    fun afterEach() {
        testFixture.stop()
    }

    @Test
    fun testCreateBasket() {
        testFixture
            .given()
            .noPriorActivity()
            .`when`()
            .command(CreateBasketCommand(basketId))
            .then()
            .success()
            .resultMessagePayload(basketId)
            .events(BasketCreatedEvent(basketId))
    }

    @Test
    fun testAddItem() {
        testFixture
            .given()
            .event(BasketCreatedEvent(basketId))
            .`when`()
            .command(AddItemCommand(basketId, item))
            .then()
            .success()
            .events(ItemAddedEvent(basketId, item))
    }

    @Test
    fun testRemoveItem() {
        testFixture
            .given()
            .events(BasketCreatedEvent(basketId), ItemAddedEvent(basketId, item))
            .`when`()
            .command(RemoveItemCommand(basketId, item.id))
            .then()
            .success()
            .events(ItemRemovedEvent(basketId, item.id))
    }

    @Test
    fun testRejectedCommand() {
        testFixture
            .given()
            .noPriorActivity()
            .`when`()
            .command(AddItemCommand(basketId, item))
            .then()
            .exception(DomainException::class.java, "Basket does not exist: $basketId")
            .noEvents()
    }
}

package community.flock.examples.axon.webshop.domain.basket

import community.flock.examples.axon.webshop.domain.event.BasketCreatedEvent
import community.flock.examples.axon.webshop.domain.event.BasketEvent
import community.flock.examples.axon.webshop.domain.event.ItemAddedEvent
import community.flock.examples.axon.webshop.domain.event.ItemRemovedEvent
import community.flock.examples.axon.webshop.domain.shared.BasketId
import community.flock.examples.axon.webshop.domain.shared.DomainException
import community.flock.examples.axon.webshop.domain.shared.ItemId
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/** Given past events, when a command, then events: the decider tested without any framework. */
class BasketDeciderTest {
    private val basketId = BasketId()
    private val item = Item(id = ItemId(), title = Title("content one"), price = Price(1.00))

    @Test
    fun `a basket is created once`() =
        runTest {
            given().decide(CreateBasketCommand(basketId)) shouldContainExactly listOf(BasketCreatedEvent(basketId))

            shouldThrow<DomainException> { given(BasketCreatedEvent(basketId)).decide(CreateBasketCommand(basketId)) }
                .problem
                .shouldBeInstanceOf<BasketAlreadyExists>()
        }

    @Test
    fun `an item is added to an existing basket`() =
        runTest {
            given(BasketCreatedEvent(basketId)).decide(AddItemCommand(basketId, item)) shouldContainExactly
                listOf(ItemAddedEvent(basketId, item))

            shouldThrow<DomainException> { given().decide(AddItemCommand(basketId, item)) }
                .problem
                .shouldBeInstanceOf<BasketNotFound>()
        }

    @Test
    fun `yolo is not a title`() =
        runTest {
            val yolo = item.copy(title = Title("YOLO"))
            shouldThrow<DomainException> { given(BasketCreatedEvent(basketId)).decide(AddItemCommand(basketId, yolo)) }
                .problem
                .shouldBeInstanceOf<TitleNotAllowed>()
        }

    @Test
    fun `only an item in the basket can be removed`() =
        runTest {
            val history = given(BasketCreatedEvent(basketId), ItemAddedEvent(basketId, item))
            history.decide(RemoveItemCommand(basketId, item.id)) shouldContainExactly listOf(ItemRemovedEvent(basketId, item.id))

            shouldThrow<DomainException> { history.decide(RemoveItemCommand(basketId, ItemId())) }
                .problem
                .shouldBeInstanceOf<ItemNotInBasket>()
        }

    @Test
    fun `the state keeps the items and their total`() {
        val other = Item(id = ItemId(), title = Title("content two"), price = Price(2.50))
        val state = given(BasketCreatedEvent(basketId), ItemAddedEvent(basketId, item), ItemAddedEvent(basketId, other))

        state.shouldBeInstanceOf<Basket.Active>().run {
            id shouldBe basketId
            items.keys shouldContainExactly setOf(item.id, other.id)
            totalPrice.toString() shouldBe "3.50"
        }
        given(BasketCreatedEvent(basketId), ItemAddedEvent(basketId, item), ItemRemovedEvent(basketId, item.id))
            .shouldBeInstanceOf<Basket.Active>()
            .items shouldBe emptyMap()
    }

    private fun given(vararg events: BasketEvent): Basket = events.fold(basketDecider.initialState, basketDecider.evolve)

    private suspend fun Basket.decide(command: BasketCommand): List<BasketEvent> = basketDecider.decide(command, this).toList()
}

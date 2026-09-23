package community.flock.examples.axon.webshop.basket.domain.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class BasketTest {
    private val basket = Basket(BasketId())

    @Test
    fun testAddAndRemoveItems() {
        val item1 = Item(title = Title("title1"), price = Price(1.00))
        val item2 = Item(title = Title("title2"), price = Price(2.50))

        val filled = basket.add(item1).add(item2)
        filled.items.values.shouldContainExactly(item1, item2)
        filled.totalPrice.toString() shouldBe "3.50"

        val emptied = filled.remove(item1.id).remove(item2.id)
        emptied.items.shouldBeEmpty()
        emptied.totalPrice.toString() shouldBe "0.00"
    }

    @Test
    fun testRemoveUnknownItemLeavesBasketUnchanged() {
        val item = Item(title = Title("title"), price = Price(1.00))
        val filled = basket.add(item)

        filled.remove(ItemId(Int.MAX_VALUE)) shouldBe filled
    }

    @Test
    fun testYoloIsRejected() {
        val yolo = Item(title = Title("YoLo"), price = Price(1.00))

        shouldThrow<IllegalArgumentException> { basket.add(yolo) }.message shouldBe "Don't yolo!!!"
        basket.items.shouldBeEmpty()
    }
}

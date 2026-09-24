package community.flock.examples.axon.webshop.app.basket

import community.flock.examples.axon.webshop.domain.basket.Item
import community.flock.examples.axon.webshop.domain.basket.Price
import community.flock.examples.axon.webshop.domain.basket.Title
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ItemTest {
    @Test
    fun testItem() {
        val item = Item(title = Title("default"), price = Price(2.00))
        item.title.value shouldBe "default"
        item.price.value.toDouble() shouldBe 2.00
    }
}

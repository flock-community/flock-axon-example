package community.flock.examples.axon.webshop.basket.domain.model

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

package community.flock.examples.axon.webshop.app.basket.query.item

import community.flock.examples.axon.webshop.app.environment.SpringBootTestWithContainers
import community.flock.examples.axon.webshop.app.second
import community.flock.examples.axon.webshop.domain.basket.Item
import community.flock.examples.axon.webshop.domain.basket.Price
import community.flock.examples.axon.webshop.domain.basket.Title
import community.flock.examples.axon.webshop.domain.shared.BasketId
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ItemRepositoryTest : SpringBootTestWithContainers() {
    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Test
    fun testItemRepository() {
        val basketId = BasketId()
        val item1 = Item(title = Title("title1"), price = Price(1.00))
        val item2 = Item(title = Title("title2"), price = Price(2.00))
        itemRepository.createBasket(basketId) shouldBe basketId
        itemRepository.getAllItemsFromBasket(basketId).shouldBeEmpty()
        itemRepository.saveItemInBasket(basketId, item1) shouldBe item1
        itemRepository.getAllItemsFromBasket(basketId).shouldHaveSize(1).first().run {
            title.value shouldBe "title1"
            price.value.toDouble() shouldBe 1.00
        }
        itemRepository.saveItemInBasket(basketId, item2) shouldBe item2
        itemRepository.getAllItemsFromBasket(basketId).shouldHaveSize(2).second().run {
            title.value shouldBe "title2"
            price.value.toDouble() shouldBe 2.00
        }
        itemRepository.deleteItemFromBasketById(basketId, item1.id)
        itemRepository.getAllItemsFromBasket(basketId).shouldHaveSize(1).first().run {
            title.value shouldBe "title2"
            price.value.toDouble() shouldBe 2.00
        }
    }
}

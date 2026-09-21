package community.flock.examples.axon.webshop.app.basket.query

import community.flock.examples.axon.webshop.api.basket.BASKET_BASE_URL
import community.flock.examples.axon.webshop.api.basket.ITEMS_PATH
import community.flock.examples.axon.webshop.api.basket.query.ItemDto
import community.flock.examples.axon.webshop.api.basket.query.QueryApi
import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.query.item.ItemProducer.produce
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.InvalidBasketIdException
import kotlinx.coroutines.future.await
import org.axonframework.config.Configuration
import org.axonframework.messaging.responsetypes.MultipleInstancesResponseType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(BASKET_BASE_URL)
class QueryController(
    configuration: Configuration,
) : QueryApi {
    private val queryGateway = configuration.queryGateway()

    @GetMapping
    override suspend fun getBasketIds(): List<String> =
        queryGateway
            .query(
                GetAllActiveBasketIds(),
                MultipleInstancesResponseType(BasketId::class.java),
            ).await()
            .map { it.toString() }

    @GetMapping("/{basketId}/$ITEMS_PATH")
    override suspend fun getItems(
        @PathVariable basketId: String,
    ): List<ItemDto> =
        run {
            val id = BasketId(basketId) ?: throw InvalidBasketIdException(basketId)
            queryGateway
                .query(
                    GetItemsQuery(id),
                    MultipleInstancesResponseType(Item::class.java),
                ).await()
        }.map { it.produce() }
}

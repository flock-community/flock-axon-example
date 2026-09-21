package community.flock.examples.axon.webshop.app.basket.query

import arrow.core.raise.either
import community.flock.examples.axon.webshop.api.endpoint.GetBasketIds
import community.flock.examples.axon.webshop.api.endpoint.GetItems
import community.flock.examples.axon.webshop.api.model.QueryProblem
import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.query.item.ItemProducer.produce
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import kotlinx.coroutines.future.await
import org.axonframework.config.Configuration
import org.axonframework.extensions.kotlin.queryMany
import org.springframework.web.bind.annotation.RestController

private interface QueryApi :
    GetBasketIds.Handler,
    GetItems.Handler

@RestController
class QueryController(
    configuration: Configuration,
) : QueryApi {
    private val queryGateway = configuration.queryGateway()

    override suspend fun getBasketIds(request: GetBasketIds.Request): GetBasketIds.Response<*> =
        GetAllActiveBasketIds()
            .let { queryGateway.queryMany<BasketId, GetAllActiveBasketIds>(it) }
            .await()
            .map { it.toString() }
            .let(GetBasketIds::Response200)

    override suspend fun getItems(request: GetItems.Request): GetItems.Response<*> =
        either {
            val basketId = BasketId(request.path.basketId).bind()
            queryGateway
                .queryMany<Item, GetItemsQuery>(GetItemsQuery(basketId))
                .await()
                .map { it.produce() }
        }.fold(
            { GetItems.Response400(QueryProblem(it.reason)) },
            { GetItems.Response200(it) },
        )
}

package community.flock.examples.axon.webshop.app.basket.query

import arrow.core.merge
import arrow.core.raise.either
import community.flock.examples.axon.webshop.api.endpoint.GetBasketIds
import community.flock.examples.axon.webshop.api.endpoint.GetItems
import community.flock.examples.axon.webshop.api.model.QueryProblem
import community.flock.examples.axon.webshop.app.basket.command.BasketIdProducer.produce
import community.flock.examples.axon.webshop.app.basket.query.item.ItemProducer.produce
import community.flock.examples.axon.webshop.app.common.SingleValidationProblem
import community.flock.examples.axon.webshop.domain.basket.Item
import community.flock.examples.axon.webshop.domain.shared.BasketId
import kotlinx.coroutines.future.await
import org.axonframework.extension.kotlin.messaging.queryMany
import org.axonframework.messaging.queryhandling.gateway.QueryGateway
import org.springframework.web.bind.annotation.RestController

private interface QueryApi :
    GetBasketIds.Handler,
    GetItems.Handler

@RestController
class QueryController(
    val queryGateway: QueryGateway,
) : QueryApi {
    override suspend fun getBasketIds(request: GetBasketIds.Request): GetBasketIds.Response<*> =
        GetAllActiveBasketIds()
            .let { queryGateway.queryMany<BasketId>(it) }
            .await()
            .produce()
            .let(GetBasketIds::Response200)

    override suspend fun getItems(request: GetItems.Request): GetItems.Response<*> =
        either {
            BasketId(request.path.basketId)
                .mapLeft { SingleValidationProblem(it.reason) }
                .bind()
                .let(::GetItemsQuery)
                .let { queryGateway.queryMany<Item>(it) }
                .await()
                .produce()
        }.map(GetItems::Response200)
            .mapLeft {
                it.reason
                    .let(::QueryProblem)
                    .let(GetItems::Response400)
            }.merge()
}

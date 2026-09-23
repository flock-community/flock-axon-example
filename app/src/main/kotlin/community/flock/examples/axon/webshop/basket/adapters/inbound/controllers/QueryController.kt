package community.flock.examples.axon.webshop.basket.adapters.inbound.controllers

import arrow.core.merge
import arrow.core.raise.either
import arrow.core.raise.withError
import community.flock.examples.axon.webshop.api.endpoint.GetBasketIds
import community.flock.examples.axon.webshop.api.endpoint.GetItems
import community.flock.examples.axon.webshop.api.model.QueryProblem
import community.flock.examples.axon.webshop.basket.adapters.inbound.controllers.BasketIdProducer.produce
import community.flock.examples.axon.webshop.basket.adapters.inbound.controllers.ItemProducer.produce
import community.flock.examples.axon.webshop.basket.application.GetAllActiveBasketIds
import community.flock.examples.axon.webshop.basket.application.GetItemsQuery
import community.flock.examples.axon.webshop.basket.domain.model.BasketId
import community.flock.examples.axon.webshop.basket.domain.model.Item
import community.flock.examples.axon.webshop.common.SingleValidationProblem
import kotlinx.coroutines.future.await
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
            .let { queryGateway.queryMany(it, BasketId::class.java) }
            .await()
            .produce()
            .let(GetBasketIds::Response200)

    override suspend fun getItems(request: GetItems.Request): GetItems.Response<*> =
        either {
            val basketId = withError(::badRequest) { parseBasketId(request.path.basketId) }
            queryGateway
                .queryMany(GetItemsQuery(basketId), Item::class.java)
                .await()
                .produce()
        }.map(GetItems::Response200).merge()

    private fun badRequest(problem: SingleValidationProblem) = GetItems.Response400(QueryProblem(problem.reason))
}

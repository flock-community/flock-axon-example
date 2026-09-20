package community.flock.examples.axon.webshop.api.basket.query

interface QueryApi :
    BasketQueryApi,
    ItemQueriesApi

interface BasketQueryApi {
    suspend fun getBasketIds(): List<String>
}

interface ItemQueriesApi {
    suspend fun getItems(basketId: String): List<ItemDto>
}

data class ItemDto(
    val id: String,
    val title: String,
    val price: String,
)

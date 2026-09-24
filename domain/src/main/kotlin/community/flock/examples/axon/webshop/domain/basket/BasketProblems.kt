package community.flock.examples.axon.webshop.domain.basket

import community.flock.examples.axon.webshop.domain.shared.BasketId
import community.flock.examples.axon.webshop.domain.shared.DomainProblem
import community.flock.examples.axon.webshop.domain.shared.ItemId

sealed class BasketProblem(
    reason: String,
) : DomainProblem(reason)

class BasketAlreadyExists(
    basketId: BasketId,
) : BasketProblem("Basket already exists: $basketId")

class BasketNotFound(
    basketId: BasketId,
) : BasketProblem("Basket does not exist: $basketId")

class ItemNotInBasket(
    basketId: BasketId,
    itemId: ItemId,
) : BasketProblem("Basket $basketId has no item with id: $itemId")

class TitleNotAllowed(
    title: Title,
) : BasketProblem("Don't $title!!!")

package community.flock.examples.axon.webshop.basket.adapters.inbound.controllers

import arrow.core.raise.Raise
import arrow.core.raise.ensureNotNull
import community.flock.examples.axon.webshop.basket.domain.model.BasketId
import community.flock.examples.axon.webshop.basket.domain.model.ItemId
import community.flock.examples.axon.webshop.common.SingleValidationProblem

class InvalidBasketId(
    id: String,
) : SingleValidationProblem("Basket id is not valid: $id")

class InvalidItemId(
    id: String,
) : SingleValidationProblem("Item id is not valid: $id")

fun Raise<SingleValidationProblem>.parseBasketId(value: String): BasketId = ensureNotNull(BasketId.parse(value)) { InvalidBasketId(value) }

fun Raise<SingleValidationProblem>.parseItemId(value: String): ItemId = ensureNotNull(ItemId.parse(value)) { InvalidItemId(value) }

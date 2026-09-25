package community.flock.examples.axon.webshop.domain.basket

import com.fraktalio.fmodel.domain.Decider
import com.fraktalio.fmodel.domain.decider
import community.flock.examples.axon.webshop.domain.event.BasketCreatedEvent
import community.flock.examples.axon.webshop.domain.event.BasketEvent
import community.flock.examples.axon.webshop.domain.event.ItemAddedEvent
import community.flock.examples.axon.webshop.domain.event.ItemRemovedEvent
import community.flock.examples.axon.webshop.domain.shared.DomainException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

/**
 * The basket, written as an fmodel decider: `decide` turns a command and the current state into the
 * events that happened, `evolve` folds an event into the state. Both are pure functions over plain
 * Kotlin types; how state is stored, loaded and locked is the concern of whatever adapter runs it.
 *
 * A command the current state cannot honour is rejected with a [DomainException] carrying the
 * [BasketProblem] that explains why.
 */
val basketDecider: Decider<BasketCommand, Basket, BasketEvent> =
    decider {
        initialState { Basket.NotCreated }

        decide { command, state ->
            when (state) {
                is Basket.NotCreated ->
                    when (command) {
                        is CreateBasketCommand -> flowOf(BasketCreatedEvent(command.basketId))
                        is AddItemCommand, is RemoveItemCommand -> reject(BasketNotFound(command.basketId))
                    }

                is Basket.Active ->
                    when (command) {
                        is CreateBasketCommand -> reject(BasketAlreadyExists(command.basketId))
                        is AddItemCommand ->
                            if (command.item.title.isForbidden()) {
                                reject(TitleNotAllowed(command.item.title))
                            } else {
                                flowOf(ItemAddedEvent(command.basketId, command.item))
                            }
                        is RemoveItemCommand ->
                            if (command.itemId in state.items) {
                                flowOf(ItemRemovedEvent(command.basketId, command.itemId))
                            } else {
                                reject(ItemNotInBasket(command.basketId, command.itemId))
                            }
                    }
            }
        }

        evolve { state, event ->
            when (state) {
                is Basket.NotCreated ->
                    when (event) {
                        is BasketCreatedEvent -> Basket.Active(event.basketId)
                        is ItemAddedEvent, is ItemRemovedEvent -> state
                    }

                is Basket.Active ->
                    when (event) {
                        is BasketCreatedEvent -> state
                        is ItemAddedEvent -> state.copy(items = state.items + (event.item.id to event.item))
                        is ItemRemovedEvent -> state.copy(items = state.items - event.itemId)
                    }
            }
        }
    }

private fun Title.isForbidden() = value.equals("yolo", ignoreCase = true)

private fun reject(problem: BasketProblem): Flow<BasketEvent> = flow { throw DomainException(problem) }

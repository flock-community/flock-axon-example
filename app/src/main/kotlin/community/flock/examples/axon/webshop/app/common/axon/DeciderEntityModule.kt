package community.flock.examples.axon.webshop.app.common.axon

import com.fraktalio.fmodel.domain.Decider
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.axonframework.common.configuration.Configuration
import org.axonframework.eventsourcing.EventSourcedEntityFactory
import org.axonframework.eventsourcing.configuration.EventSourcedEntityModule
import org.axonframework.eventsourcing.eventstore.PayloadBasedTagResolver
import org.axonframework.eventsourcing.eventstore.TagResolver
import org.axonframework.messaging.commandhandling.CommandResultMessage
import org.axonframework.messaging.commandhandling.GenericCommandResultMessage
import org.axonframework.messaging.core.Message
import org.axonframework.messaging.core.MessageStream
import org.axonframework.messaging.core.MessageTypeResolver
import org.axonframework.messaging.core.QualifiedName
import org.axonframework.messaging.core.conversion.MessageConverter
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.messaging.eventstreaming.EventCriteria
import org.axonframework.messaging.eventstreaming.Tag
import org.axonframework.modelling.EntityIdResolver
import kotlin.reflect.KClass

/**
 * An fmodel [Decider] run by Axon as an event-sourced entity, plus the [TagResolver] that tags the
 * decider's events with the identifier of the entity they belong to. Register it as a Spring bean:
 * Axon picks up the module, and [DeciderAxonConfig] combines the tag resolvers of every decider.
 */
class DeciderEntityModule<ID : Any, S : Any> internal constructor(
    module: EventSourcedEntityModule<ID, S>,
    val tagResolver: TagResolver,
) : EventSourcedEntityModule<ID, S> by module

/**
 * Runs this decider as an Axon event-sourced entity.
 *
 * Every concrete command type is an instance command: Axon loads the entity's events by [tagKey]
 * and the id [commandId] takes from the command, folds them with the decider's `evolve` starting
 * from its `initialState`, and hands the state to `decide`. The events `decide` emits are appended,
 * tagged with [tagKey] and the id [eventId] takes from each of them. A command handler answers with
 * the id. Command and event types are expected to be sealed hierarchies, because a message that
 * arrives serialized is converted to its concrete class.
 */
inline fun <reified C : Any, reified S : Any, reified E : Any, reified ID : Any> Decider<C, S, E>.asEventSourcedEntity(
    tagKey: String,
    noinline commandId: (C) -> ID,
    noinline eventId: (E) -> ID,
): DeciderEntityModule<ID, S> = deciderEntityModule(this, C::class, S::class, E::class, ID::class, tagKey, commandId, eventId)

fun <C : Any, S : Any, E : Any, ID : Any> deciderEntityModule(
    decider: Decider<C, S, E>,
    commandType: KClass<C>,
    stateType: KClass<S>,
    eventType: KClass<E>,
    idType: KClass<ID>,
    tagKey: String,
    commandId: (C) -> ID,
    eventId: (E) -> ID,
): DeciderEntityModule<ID, S> {
    val commands = commandType.concreteTypes()
    val events = eventType.concreteTypes()

    val module =
        EventSourcedEntityModule
            .declarative(idType.java, stateType.java)
            .messagingModel { configuration, builder ->
                val converter = configuration.converter()
                val commandsByName = configuration.byMessageName(commands)
                val eventsByName = configuration.byMessageName(events)
                val resultType = configuration.getComponent(MessageTypeResolver::class.java).resolveOrThrow(idType.java)
                commandsByName.entries
                    .fold(builder) { model, (name, type) ->
                        model.instanceCommandHandler(name) { message, state, context ->
                            try {
                                val command = message.payload(type, converter)
                                val emitted = runBlocking { decider.decide(command, state).toList() }
                                EventAppender.forContext(context).append(emitted)
                                MessageStream.just<CommandResultMessage>(GenericCommandResultMessage(resultType, commandId(command)))
                            } catch (e: Exception) {
                                MessageStream.failed(e)
                            }
                        }
                    }.entityEvolver { state, message, _ ->
                        eventsByName[message.type().qualifiedName()]
                            ?.let { decider.evolve(state, message.payload(it, converter)) }
                            ?: state
                    }.build()
            }.entityFactory(EventSourcedEntityFactory.fromIdentifier<ID, S> { decider.initialState })
            .criteriaResolver { id, _ -> EventCriteria.havingTags(Tag.of(tagKey, id.toString())) }
            .entityIdResolver { configuration ->
                val converter = configuration.converter()
                val commandsByName = configuration.byMessageName(commands)
                EntityIdResolver { message, _ -> commandId(message.payload(commandsByName.getValue(message.name()), converter)) }
            }.build()

    val tagResolver =
        PayloadBasedTagResolver
            .forPayloadType(eventType.java)
            .withResolver { Tag.of(tagKey, eventId(it).toString()) }

    return DeciderEntityModule(module, tagResolver)
}

/** The leaves of a sealed hierarchy, or the type itself when it is not sealed. */
private fun <T : Any> KClass<T>.concreteTypes(): List<KClass<out T>> =
    if (isSealed) sealedSubclasses.flatMap { it.concreteTypes() } else listOf(this)

private fun Configuration.converter(): MessageConverter = getComponent(MessageConverter::class.java)

private fun <T : Any> Configuration.byMessageName(types: List<KClass<out T>>): Map<QualifiedName, KClass<out T>> =
    getComponent(MessageTypeResolver::class.java).let { resolver -> types.associateBy { resolver.resolveOrThrow(it.java).qualifiedName() } }

private fun Message.name(): QualifiedName = type().qualifiedName()

/** The payload as the given type: as it is when it was dispatched in this process, converted when it arrived serialized. */
@Suppress("UNCHECKED_CAST")
private fun <T : Any> Message.payload(
    type: KClass<out T>,
    converter: MessageConverter,
): T = requireNotNull(payloadAs(type.java as Class<T>, converter)) { "Message ${name()} has no payload" }

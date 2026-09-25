package community.flock.examples.axon.webshop.app.common.axon

import org.axonframework.eventsourcing.eventstore.MultiTagResolver
import org.axonframework.eventsourcing.eventstore.TagResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DeciderAxonConfig {
    /** One tag resolver for the event store, made of the tag resolver of every decider entity. */
    @Bean
    fun tagResolver(entities: List<DeciderEntityModule<*, *>>): TagResolver = MultiTagResolver(entities.map { it.tagResolver })
}

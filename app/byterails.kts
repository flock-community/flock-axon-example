import community.flock.byterails.dsl.byterails

byterails {
    // Rules of the module root, where the application class lives, inherited by every package of
    // the app: the messaging framework, typed errors, coroutines and the Spring stereotypes.
    allow("io.axoniq")
    allow("org.axonframework")
    allow("arrow")
    allow("kotlinx.coroutines")
    allow("org.springframework.stereotype")

    // The entry point boots Spring, which hands back an application context, and enables the
    // wirespec controllers.
    allow("org.springframework.boot")
    allow("org.springframework.context")
    allow("community.flock.wirespec.integration.spring")

    // No package inherits the messaging framework as a whole. Each package below names the part
    // of Axon it adapts, and the parts that model state are owned exclusively, so nothing else in
    // the build, the domain module included, can reach them.

    // Transformer and converter interfaces and the validation problems, shared by every slice.
    pkg("common")

    // The bridge from an fmodel decider to an Axon event-sourced entity: the only code that builds
    // entity models, repositories, criteria and tags.
    pkg("common.axon") {
        allow("com.fraktalio.fmodel")
        allow("org.axonframework.common.configuration")
        allow("org.axonframework.conversion")
        allow("org.axonframework.messaging")
        exclusive("org.axonframework.eventsourcing")
        exclusive("org.axonframework.modelling")
    }

    // Each slice is one decider with a command side and a query side; the query side reads the
    // command side's producers, never the other way round.
    slice {
        allow("common")

        // The command side hands its decider to the bridge and dispatches commands from its
        // controllers: the gateway, and the exception a rejected command comes back as, are all it
        // sees of Axon.
        pkg("command") {
            allow("com.fraktalio.fmodel")
            allow("org.springframework.web.bind.annotation")
            allow("org.axonframework.messaging.commandhandling.gateway")
            allow("org.axonframework.messaging.commandhandling.CommandExecutionException")
            allow("org.axonframework.extension.kotlin.messaging")
        }

        // The query side holds the projections, the only event and query handlers of the app, and
        // the controllers that query them.
        pkg("query") {
            allow("command")
            allow("org.springframework.web.bind.annotation")
            allow("org.axonframework.messaging.queryhandling.gateway")
            allow("org.axonframework.extension.kotlin.messaging")
            exclusive("org.axonframework.messaging.eventhandling.annotation")
            exclusive("org.axonframework.messaging.queryhandling.annotation")
            exclusive("jakarta.persistence")
            exclusive("org.springframework.data")
        }
    }
}

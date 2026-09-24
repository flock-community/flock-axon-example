import community.flock.byterails.dsl.byterails

byterails {
    // The event-sourced model, written with fmodel: the basket decider with its commands and state,
    // the events it emits and the shared value types. Every other module may use all three packages.
    exported("basket")
    exported("event")
    exported("shared")

    // The decider and view types, the Flow the decider emits its events through, and typed errors.
    // No messaging framework: the Axon adapters live in the app module, and the app module's
    // exclusives on org.axonframework make sure it stays that way.
    allow("com.fraktalio.fmodel")
    allow("kotlinx.coroutines.flow")
    allow("arrow")

    pkg("shared")
    pkg("event") {
        allow("basket")
        allow("shared")
    }
    pkg("basket") {
        allow("event")
        allow("shared")
    }
}

byterails {
    // The event-sourced model: the aggregate with its commands, the events and the shared value
    // types. Every other module may use all three packages.
    exported("basket")
    exported("event")
    exported("shared")

    // The messaging framework for the handler annotations, and typed errors.
    allow("org.axonframework")
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

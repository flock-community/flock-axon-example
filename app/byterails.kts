import community.flock.byterails.dsl.byterails

byterails {
    // Rules of the module root, where the application class lives, inherited by every package of
    // the app: the messaging framework, typed errors, coroutines and the Spring stereotypes.
    allow("org.axonframework")
    allow("arrow")
    allow("kotlinx.coroutines")
    allow("org.springframework.stereotype")

    // The entry point boots Spring, which hands back an application context, and enables the
    // wirespec controllers.
    allow("org.springframework.boot")
    allow("org.springframework.context")
    allow("community.flock.wirespec.integration.spring")

    // Transformer and converter interfaces and the validation problems, shared by every slice.
    pkg("common")

    // Each slice is one aggregate with a command side and a query side; the query side reads the
    // command side's producers, never the other way round.
    slice {
        allow("common")
        pkg("command") {
            allow("org.springframework.web.bind.annotation")
        }
        pkg("query") {
            allow("command")
            allow("org.springframework.web.bind.annotation")
            exclusive("jakarta.persistence")
            exclusive("org.springframework.data")
        }
    }
}

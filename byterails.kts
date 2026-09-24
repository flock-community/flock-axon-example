import community.flock.byterails.dsl.byterails
import community.flock.byterails.rules.kotlin

byterails {
    // Every package of this project lies in one of the modules api, domain and app, each declared
    // by the rules file next to its pom. The root file keeps only what every module shares.

    // The Kotlin and Java standard libraries, allowed in every package.
    kotlin()
}

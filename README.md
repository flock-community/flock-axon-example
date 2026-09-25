# Flock Axon Example

## Webshop

### Architecture

The business logic is written with [fmodel](https://github.com/fraktalio/fmodel): the `domain`
module holds a `Decider` for the basket, whose `decide` turns a command and the current state into
events and whose `evolve` folds an event into the state. Both are pure functions over plain Kotlin
types, tested without any framework in `BasketDeciderTest`.

Axon lives in the `app` module only. `common.axon` bridges any decider to an Axon event-sourced
entity: it registers every concrete command of the decider as an instance command handler, loads
the entity's events by tag, folds them with `evolve` and appends what `decide` emits. Each slice
registers its decider as one Spring bean (`BasketCommandModel`), the controllers dispatch through
the command and query gateways, and the projections are the only event and query handlers.

[byterails](https://github.com/flock-community/byterails) keeps it that way: the domain module never
allows `org.axonframework`, and the app module's rules make `common.axon` the exclusive owner of
Axon's event sourcing and modelling packages, so no other package of the build can reach them.

### Requirements

* Docker
* JDK 21

### Quick Start

```shell
docker compose up -d
```

[Complete Axon installation](http://localhost:8024/)

```shell
make all run
```

### Cleanup

```shell
docker compose down
```

### Testing (IntelliJ)

Find a list of operations for all commands and queries in the folder `.test/`. For this to work create a
`http-client.private.env.json` with the following content:

```json
{
  "local": {
    "basketId": "7e21babe-6829-49c2-af56-74bb649f9757"
  }
}
```

Fill in the `basketId` with the id of a newly created basket. 

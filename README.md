# Flock Axon Example

## Webshop

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

### Architecture guardrails

The packages follow the `hexagonalSpring` layout of [byterails](https://github.com/flock-community/byterails), which
`make build` checks on the compiled classes of every module (Maven's `verify` phase). The rule sets, the base package
and the slice `basket` are configured in `pom.xml`; `byterails.kts` widens the layout with the libraries this service
uses. The generated `api` module is not checked.

| Package under `community.flock.examples.axon.webshop` | Module   | Holds                                                                                   |
|-------------------------------------------------------|----------|-----------------------------------------------------------------------------------------|
| `WebshopApplication`                                  | `app`    | the Spring Boot entry point                                                             |
| `common`                                              | `app`    | the transformer and converter interfaces and the validation problems                    |
| `basket.domain.model`                                 | `domain` | `Basket`, `Item`, `Price`, `Title` and the ids; no framework, no library                |
| `basket.domain.ports`                                 | `domain` | what the application layer needs from the outside, as `*Port` interfaces                |
| `basket.application`                                  | `app`    | the Axon command model with its commands, events and queries, and the projections       |
| `basket.adapters.inbound.controllers`                 | `app`    | the REST controllers implementing the wirespec contract of the `api` module             |
| `basket.adapters.outbound.database`                   | `app`    | the JPA read model: the adapter, its entities in `.model` and the mappers in `.mappers` |
| `basket.adapters.outbound.memory`                     | `app`    | the in-memory list of active baskets                                                    |

A violation fails the build and names the class, the reference and the rule that decided it. While moving code around,
`./mvnw verify -Dbyterails.reportOnly=true` prints the violations and keeps the build green.

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

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

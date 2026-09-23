package community.flock.examples.axon.webshop

import community.flock.wirespec.integration.spring.kotlin.configuration.EnableWirespecController
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableWirespecController
class WebshopApplication

fun main(args: Array<String>) {
    runApplication<WebshopApplication>(*args)
}

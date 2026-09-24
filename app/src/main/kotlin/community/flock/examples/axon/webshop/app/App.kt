package community.flock.examples.axon.webshop.app

import community.flock.wirespec.integration.spring.kotlin.configuration.EnableWirespecController
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableWirespecController
@SpringBootApplication(scanBasePackages = ["community.flock.examples.axon.webshop"])
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}

package community.flock.examples.axon.webshop.app.environment

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.mongodb.MongoDBContainer
import org.testcontainers.postgresql.PostgreSQLContainer
import java.time.Duration

@Testcontainers
abstract class WithContainers {
    @Test
    fun containersAreRunning() {
        axonServer.isRunning shouldBe true
        postgresql.isRunning shouldBe true
        mongoDB.isRunning shouldBe true
    }

    companion object {
        private const val WAIT_IN_SECONDS = 10L

        private val axonServer: GenericContainer<*> =
            GenericContainer("axoniq/axonserver")
                .withExposedPorts(8024, 8124)
                .waitingFor(Wait.forHttp("/actuator/info").forPort(8024))
                .withStartupTimeout(Duration.ofSeconds(WAIT_IN_SECONDS))
                .apply { start() }

        private val mongoDB: MongoDBContainer =
            MongoDBContainer("mongo:$MONGODB_VERSION")
                .apply { start() }

        private val postgresql: PostgreSQLContainer =
            PostgreSQLContainer("postgres:$POSTGRESQL_VERSION")
                .withUsername("supergebruiker")
                .withPassword("kapotgeheim")
                .apply { start() }

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            val mongoDBString = "mongodb://${mongoDB.host}:${mongoDB.getMappedPort(27017)}/items"
            val postgresString = "jdbc:postgresql://${postgresql.host}:${postgresql.getMappedPort(5432)}/postgres"
            val axonString = "${axonServer.host}:${axonServer.getMappedPort(8124)}"

            System.setProperty("axon.axonserver.servers", axonString)
            System.setProperty("spring.datasource.url", postgresString)
            System.setProperty("spring.data.mongodb.uri", mongoDBString)
        }
    }
}

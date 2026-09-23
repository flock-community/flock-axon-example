package community.flock.examples.axon.webshop.environment

import org.axonframework.test.fixture.MessagesRecordingConfigurationEnhancer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConfig {
    @Bean
    fun recordingEnhancer() = MessagesRecordingConfigurationEnhancer()
}

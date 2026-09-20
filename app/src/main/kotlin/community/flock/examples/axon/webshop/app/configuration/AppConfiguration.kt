package community.flock.examples.axon.webshop.app.configuration

import org.axonframework.common.jdbc.DataSourceConnectionProvider
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore
import org.axonframework.eventhandling.tokenstore.jdbc.PostgresTokenTableFactory.INSTANCE
import org.axonframework.eventhandling.tokenstore.jdbc.TokenSchema
import org.axonframework.serialization.Serializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class AppConfiguration {
    @Bean
    fun tokenStore(
        dataSource: DataSource,
        serializer: Serializer,
    ): TokenStore =
        dataSource
            .let(::DataSourceConnectionProvider)
            .let(JdbcTokenStore.builder()::connectionProvider)
            .serializer(serializer)
            .schema(tokenSchema())
            .build()
            .apply { createSchema(INSTANCE) }

    private fun tokenSchema() =
        TokenSchema
            .builder()
            .setTokenTable("TOKEN_ENTRY")
            .setProcessorNameColumn("PROCESSOR_NAME")
            .setSegmentColumn("SEGMENT")
            .setTokenColumn("TOKEN")
            .setTokenTypeColumn("TOKEN_TYPE")
            .setTimestampColumn("TOKEN_DT")
            .setOwnerColumn("OWNER")
            .build()
}

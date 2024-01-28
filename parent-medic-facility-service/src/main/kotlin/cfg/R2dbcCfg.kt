package cfg

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate

@Configuration
class R2dbcCfg {

    @Value("\${spring.r2dbc.url}")
    private lateinit var url: String

    @Bean
    fun connectionFactory() = ConnectionFactories.get(url)

    @Bean
    fun r2dbcEntityTemplate() = R2dbcEntityTemplate(connectionFactory())
}
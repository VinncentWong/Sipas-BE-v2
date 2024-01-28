package cfg

import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.flyway.FlywayProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(value = [
    FlywayProperties::class
])
class FlywayCfg {

    @Bean(initMethod = "migrate")
    fun flywayCfg(
        properties: FlywayProperties
    ): Flyway{
        return Flyway
            .configure()
            .baselineOnMigrate(true)
            .dataSource(
                properties.url,
                properties.user,
                properties.password
            )
            .load()
    }
}
package org.example.cfg;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        FlywayProperties.class
})
public class MysqlCfg {

    @Bean(initMethod = "migrate")
    public Flyway flyway(FlywayProperties props){
        return Flyway.configure()
                .dataSource(
                        props.getUrl(),
                        props.getUser(),
                        props.getPassword()
                )
                .baselineOnMigrate(true)
                .load();
    }
}

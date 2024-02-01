package org.example.parent_medic.cfg;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        FlywayProperties.class
})
public class FlywayCfg {

    @Bean(initMethod = "migrate")
    public Flyway flywayCfgParentMedic(FlywayProperties props){
        return Flyway
                .configure()
                .baselineOnMigrate(true)
                .dataSource(
                        props.getUrl(),
                        props.getUser(),
                        props.getPassword()
                )
                .load();
    }
}

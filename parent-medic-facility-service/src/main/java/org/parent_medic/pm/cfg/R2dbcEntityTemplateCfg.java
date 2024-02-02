package org.parent_medic.pm.cfg;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

@Configuration
public class R2dbcEntityTemplateCfg {

    @Value("${spring.r2dbc.url}")
    private String r2dbcUrl;

    @Bean
    public ConnectionFactory connectionFactory(){
        return ConnectionFactories
                .get(r2dbcUrl);
    }

    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(){
        return new R2dbcEntityTemplate(connectionFactory());
    }
}

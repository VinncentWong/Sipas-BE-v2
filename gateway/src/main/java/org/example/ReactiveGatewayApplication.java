package org.example;

import org.example.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// https://www.alibabacloud.com/help/en/edas/developer-reference/build-service-gateways
@SpringBootApplication
public class ReactiveGatewayApplication {

    @Autowired
    private JwtFilter filter;

    public static void main(String[] args) {
        SpringApplication.run(ReactiveGatewayApplication.class, args);
    }
}
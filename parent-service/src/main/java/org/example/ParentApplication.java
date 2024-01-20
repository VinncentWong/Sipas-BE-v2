package org.example;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDubbo
@DubboComponentScan

// https://www.alibabacloud.com/help/en/edas/developer-reference/implement-service-registration-and-discovery#Task72618
// tell Spring Cloud Alibaba to register this application into Nacos
@EnableDiscoveryClient
public class ParentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParentApplication.class, args);
    }
}
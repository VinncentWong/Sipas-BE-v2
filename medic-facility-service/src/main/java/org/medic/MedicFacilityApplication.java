package org.medic;


import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {
        "org.medic"
})
@EnableDubbo
@DubboComponentScan
@EnableDiscoveryClient
public class MedicFacilityApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicFacilityApplication.class, args);
    }
}
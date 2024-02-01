package org.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {
        "org.example.medic_facility"
})
@EnableDiscoveryClient
public class MedicFacilityApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicFacilityApplication.class, args);
    }
}
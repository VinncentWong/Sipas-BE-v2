package org.example;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "org.example.parent_medic"
})
@EnableDubbo
@DubboComponentScan
public class ParentMedicFacilityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParentMedicFacilityApplication.class, args);
    }
}

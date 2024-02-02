package org.parent_medic;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication(scanBasePackages = {
        "org.parent_medic"
})
@EnableR2dbcRepositories(
    basePackages = {
        "org.parent_medic"
    }
)
@EnableDubbo
@DubboComponentScan
public class ParentMedicFacilityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParentMedicFacilityApplication.class, args);
    }
}

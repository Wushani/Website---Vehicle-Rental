package org.web.autolanka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "org.web.autolanka.entity")
@EnableJpaRepositories(basePackages = "org.web.autolanka.repository")
public class AutolankaRentalApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutolankaRentalApplication.class, args);
    }
}
package com.alphateam6.carinsurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CarinsuranceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarinsuranceApplication.class, args);
    }

}

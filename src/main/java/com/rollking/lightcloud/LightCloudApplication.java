package com.rollking.lightcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
@SpringBootApplication
public class LightCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(LightCloudApplication.class, args);
    }

}

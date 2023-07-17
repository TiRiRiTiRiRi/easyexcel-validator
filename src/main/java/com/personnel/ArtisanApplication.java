package com.personnel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.personnel.mapper"})
public class ArtisanApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtisanApplication.class, args);
    }

}

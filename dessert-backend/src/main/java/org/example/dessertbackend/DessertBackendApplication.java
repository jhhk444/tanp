package org.example.dessertbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.dessertbackend.mapper") // 必须加！扫描所有Mapper接口
public class DessertBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(DessertBackendApplication.class, args);
    }
}
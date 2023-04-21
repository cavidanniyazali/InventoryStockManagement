package com.example.inventorystockmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories(basePackages="com.example.inventorystockmanagement")
//@ComponentScan(basePackages = {"com.example.inventorystockmanagement"})
public class InventoryStockManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryStockManagementApplication.class, args);
    }

}

package com.pepcus.appstudent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
@EnableCaching
@SpringBootApplication
public class Application {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

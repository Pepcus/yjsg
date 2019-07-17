package com.pepcus.appstudent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Application start class
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
@EnableCaching
@SpringBootApplication
public class Application {

    /**
     * Its main method to bootstrap the application
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

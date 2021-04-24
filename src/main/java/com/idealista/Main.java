package com.idealista;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String [] args){
        configureSystem();
        SpringApplication.run(Main.class, args);
    }

    /**
     * Make sure the JVM is using UTC in Date
     */
    private static void configureSystem() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
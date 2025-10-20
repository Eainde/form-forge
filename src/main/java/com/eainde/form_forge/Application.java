package com.eainde.form_forge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.eainde.form_forge")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

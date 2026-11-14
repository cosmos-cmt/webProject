package com.cuda.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CudaGymApplication {

        public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CudaGymApplication.class);

                ConfigurableApplicationContext context = application.run(args);

                String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        if (activeProfiles.length == 0) {
            System.out.println("CUDA Gym started with default profile.");
        } else {
            System.out.println("CUDA Gym started with profiles: " + String.join(", ", activeProfiles));
        }
    }
}
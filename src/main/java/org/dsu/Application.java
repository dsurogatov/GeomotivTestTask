package org.dsu;

import java.util.Arrays;

import org.dsu.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private TestService testService;

    @Override
    public void run(String... args) throws Exception {
        testService.test();
    }

    public static void main(String[] args) {
        System.out.println("Args: " + Arrays.toString(args));
        SpringApplication.run(Application.class, args);
    }

}

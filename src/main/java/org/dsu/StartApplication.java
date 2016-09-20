package org.dsu;

import org.springframework.boot.SpringApplication;

public class StartApplication {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage application with options:\n" 
                    + "arg1      the folder name with input files\n"
                    + "arg2      the output file name");
            return;
        }
        
        SpringApplication.run(Application.class, args);
    }

}

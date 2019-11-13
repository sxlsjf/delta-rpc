package com.sxl.sample.test.provider;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: shenxl
 * @Date: 2019/11/12 14:25
 * @Version 1.0
 * @description：${description}
 */
@Component
public class WelcomeCommandLineRunner implements CommandLineRunner {


    public void run(String... args) throws Exception {
        System.out.println("***** WELCOME TO THE DEMO *****");
    }

}

package com.sxl.sample.test.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.sxl.rpc.ann.ServiceScan;

@SpringBootApplication
@ServiceScan(basePackage = {"com.sxl.sample.test.provider"})
public class TestProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestProviderApplication.class, args);
    }

}

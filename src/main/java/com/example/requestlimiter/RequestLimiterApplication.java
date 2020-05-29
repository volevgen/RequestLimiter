package com.example.requestlimiter;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RequestLimiterApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(RequestLimiterApplication.class, args);
    }

}

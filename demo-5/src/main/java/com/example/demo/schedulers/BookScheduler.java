package com.example.demo.schedulers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookScheduler {

    @Scheduled(fixedRate = 5000)
    public void checkBookAvailability() {
        System.out.println("Checking book inventory...");
    }
}

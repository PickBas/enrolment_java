package com.sayed.enrolment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnrolmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnrolmentApplication.class, args);
    }

//    @PostConstruct
//    public void init(){
//        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
//    }
}

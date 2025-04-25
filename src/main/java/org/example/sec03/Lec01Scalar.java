package org.example.sec03;

import org.example.models.sec03.Person;
import org.example.sec02.ProtoDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01Scalar {
    private static final Logger log = LoggerFactory.getLogger(ProtoDemo.class);
    public static void main(String[] args) {

        var person = Person.newBuilder()
                .setLastName("same")
                .setAge(32)
                .setEmail("sam@gmail.com")
                .setEmployed(true)
                .setSalary(325432.56)
                .setBankAccountNumber(4234234)
                .setBalance(-2342)
                .build();
        log.info("Person: {}",person);



    }
}

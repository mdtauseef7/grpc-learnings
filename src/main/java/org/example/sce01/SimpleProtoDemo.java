package org.example.sce01;

import org.example.models.sec02.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleProtoDemo {

    private static final Logger log = LoggerFactory.getLogger(SimpleProtoDemo.class);
    public static void main(String[] args) {
        Person person = Person.newBuilder()
                .setName("Tauseef")
                .setAge(35)
                .build();

        log.info("Person is : [{}]",person);

    }
}

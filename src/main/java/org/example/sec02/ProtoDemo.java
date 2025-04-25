package org.example.sec02;

import org.example.models.sec02.Person;
import org.example.sce01.SimpleProtoDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoDemo {
    private static final Logger log = LoggerFactory.getLogger(ProtoDemo.class);

    public static void main(String[] args) {

        Person p1 = createPerson();

        Person p2 = createPerson();

       log.info("Equals Method, {}",p1.equals(p2));
        log.info("== Operator, {}", (p1==p2));







    }


    private static Person createPerson(){
        return Person.newBuilder().setName("Tauseef").setAge(35).build();
    }
}

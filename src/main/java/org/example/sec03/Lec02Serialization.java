package org.example.sec03;

import org.example.models.sec03.Person;
import org.example.sec02.ProtoDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Lec02Serialization {
    private static final Logger log = LoggerFactory.getLogger(ProtoDemo.class);
    private static final Path PATH = Path.of("person.out");
    public static void main(String[] args) throws IOException {
        var person = Person.newBuilder()
                /*.setLastName("same")
                .setAge(32)
                .setEmail("sam@gmail.com") */
                .setEmployed(false)
                /*.setSalary(325432.56)
                .setBankAccountNumber(4234234L)
                .setBalance(-2342)*/
                .build();


        serialize(person);
        log.info("{}",deSerialize());
       // log.info("equals {}",person.equals(deSerialize()));

        log.info("person length: {}",person.toByteArray());
    }

    public static void serialize(Person person) throws IOException {
        try(var stream = Files.newOutputStream(PATH);){
            person.writeTo(stream);

        }

    }

    private static Person  deSerialize() throws IOException {
      try(var stream = Files.newInputStream(PATH)){
           return Person.parseFrom(stream);
        }
    }
}

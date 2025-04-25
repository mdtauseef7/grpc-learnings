package org.example.common;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractChannelTest {
    protected ManagedChannel channel;

    @BeforeAll
    public void setupChannel(){
        this.channel = ManagedChannelBuilder.forAddress("localhost",6565)
                .usePlaintext().build();
    }

    @AfterAll
    public void stopChannel(){
        try {
            this.channel.shutdown()
                    .awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
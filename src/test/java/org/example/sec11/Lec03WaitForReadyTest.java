package org.example.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.Deadline;
import org.example.common.AbstractChannelTest;
import org.example.common.GrpcServer;
import org.example.models.sec11.BankServiceGrpc;
import org.example.models.sec11.WithdrawRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Lec03WaitForReadyTest extends AbstractChannelTest {

    private static final Logger log = LoggerFactory.getLogger(Lec03WaitForReadyTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub stub;


    @BeforeAll
    public void setup(){
       //
        Runnable runnable = ()->{
            Uninterruptibles.sleepUninterruptibly(5,TimeUnit.SECONDS);
            this.grpcServer.start();
        };
        Thread.ofVirtual().start(runnable);

        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
        this.stub = BankServiceGrpc.newStub(channel);

    }

    @Test
    public void clientWaitForReadyTest() {
       log.info("client sent the request");
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(50)
                .build();

        var response = this.blockingStub
                .withWaitForReady()
                .withDeadline(Deadline.after(18, TimeUnit.SECONDS))
                .withdraw(request);

        while (response.hasNext()) {
            log.info("{}", response.next());
        }
    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }
}

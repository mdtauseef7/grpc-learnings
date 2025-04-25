package org.example.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import org.example.common.AbstractChannelTest;
import org.example.common.GrpcServer;
import org.example.models.sec11.BalanceCheckRequest;
import org.example.models.sec11.BankServiceGrpc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Lec06KeepAliveDemoTest extends AbstractChannelTest {
    private static final Logger log = LoggerFactory.getLogger(Lec06KeepAliveDemoTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    private BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void lazyChannelDemo(){
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        var response = this.bankServiceBlockingStub.getAccountBalance(request);
        log.info("{}",response);

        Uninterruptibles.sleepUninterruptibly(30, TimeUnit.SECONDS);
    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }
}

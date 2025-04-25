package org.example.sec06;

import ch.qos.logback.core.util.TimeUtil;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.models.sec06.AccountBalance;
import org.example.models.sec06.BalanceCheckRequest;
import org.example.models.sec06.BankServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

public class GrpcClient {
    private static final Logger log = LoggerFactory.getLogger(GrpcClient.class);

    public static void main(String[] args) {

        var channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                        .usePlaintext()
                        .build();


        /*var stub = BankServiceGrpc.newBlockingStub(channel);

        AccountBalance accountBalance = stub.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(5).build());
        log.info("accountBalance: {}",accountBalance);*/


        BankServiceGrpc.BankServiceStub bankServiceStub = BankServiceGrpc.newStub(channel);

        bankServiceStub.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(5).build(), new StreamObserver<AccountBalance>() {
            @Override
            public void onNext(AccountBalance accountBalance) {
                log.info("accountBalance: {}",accountBalance);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                log.info("======Completed==========");
            }
        });

        log.info("Done");
        try {
            Thread.sleep(Duration.ofSeconds(1));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}

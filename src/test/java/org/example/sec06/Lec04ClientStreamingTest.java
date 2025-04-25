package org.example.sec06;

import io.grpc.stub.StreamObserver;
import org.example.common.ResponseObserver;
import org.example.models.sec06.AccountBalance;
import org.example.models.sec06.DepositRequest;
import org.example.models.sec06.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

public class Lec04ClientStreamingTest extends AbstractTest{

    @Test
    public void depositTest(){

        var responseObserver = ResponseObserver.<AccountBalance>create();
        var requestObserver = this.stub.deposit(responseObserver);

        requestObserver.onNext(DepositRequest.newBuilder().setAccountNumber(5).build());

        IntStream.rangeClosed(1,10)
                .mapToObj(i-> Money.newBuilder().setAmount(10).build())
                .map(m-> DepositRequest.newBuilder().setMoney(m).build())
                .forEach(requestObserver::onNext);

        requestObserver.onCompleted();

        responseObserver.await();

        Assertions.assertEquals(1, responseObserver.getItems().size());
        Assertions.assertEquals(200, responseObserver.getItems().getFirst().getBalance());
    }

}

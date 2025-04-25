package org.example.sec12;

import org.example.common.ResponseObserver;
import org.example.models.sec12.Money;
import org.example.models.sec12.WithdrawRequest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;

public class Lec02ExecutorCallOptionTest extends AbstractTest{

    @Test
    public void executorDemo(){
        var observer = ResponseObserver.<Money>create();

        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(30)
                .build();

        this.stub
                .withExecutor(Executors.newVirtualThreadPerTaskExecutor())
                .withdraw(request,observer);
        observer.await();

    }
}

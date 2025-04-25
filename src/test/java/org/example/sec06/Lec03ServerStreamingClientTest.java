package org.example.sec06;

import com.google.protobuf.Empty;
import org.example.common.ResponseObserver;
import org.example.models.sec06.AccountBalances;
import org.example.models.sec06.Money;
import org.example.models.sec06.WithdrawRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class Lec03ServerStreamingClientTest extends AbstractTest{


    private static final Logger log = LoggerFactory.getLogger(Lec03ServerStreamingClientTest.class);

    @Test
    public void blockingClientWithdrawTest(){
        var request = WithdrawRequest.newBuilder()
           .setAccountNumber(2)
           .setAmount(20)
           .build();

        Iterator<Money> moneyIterable = this.blockingStub.withdraw(request);

        int count = 0;
        while (moneyIterable.hasNext()){
            log.info("received money: {}",moneyIterable.next());
            count++;
        }

        Assertions.assertEquals(2,count);
    }

    @Test
    public void asyncClientWithdrawTest(){
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(2)
                .setAmount(20)
                .build();

        var observer = ResponseObserver.<Money>create();
        this.stub.withdraw(request,observer);
        observer.await();

        Assertions.assertEquals(1,observer.getItems().size());
        Assertions.assertNull(observer.getThrowable());

    }



}

package org.example.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.Deadline;
import org.example.common.ResponseObserver;
import org.example.models.sec11.Money;
import org.example.models.sec11.WithdrawRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Lec02ServerStreamingDeadlineTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(Lec02ServerStreamingDeadlineTest.class);

    @Test
    public void blockingServerStreamingDeadlineTest(){
        try {
            var request = WithdrawRequest.newBuilder()
                    .setAccountNumber(1)
                    .setAmount(50)
                    .build();

            var response = this.blockingStub.withDeadline(Deadline.after(2, TimeUnit.SECONDS)).withdraw(request);

            while (response.hasNext()){
                log.info("{}",response.next());
            }
        } catch (Exception e) {
            log.info("Error");
        }
        Uninterruptibles.sleepUninterruptibly(10,TimeUnit.SECONDS);
    }

    @Test
    public void asyncServerStreamingDeadlineTest(){
        var observer = ResponseObserver.<Money>create();
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(50)
                .build();

        this.stub.withDeadline(Deadline.after(3, TimeUnit.SECONDS)).withdraw(request,observer);
        observer.await();

        Assertions.assertEquals(1, observer.getItems().size());
    }
}

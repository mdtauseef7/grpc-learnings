package org.example.sec12;

import io.grpc.ClientInterceptor;
import io.grpc.Deadline;
import org.example.common.ResponseObserver;
import org.example.models.sec12.Money;
import org.example.models.sec12.WithdrawRequest;
import org.example.models.sec12.BalanceCheckRequest;
import org.example.sec12.interceptor.DeadlineInterceptor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Lec03DeadlineInterceptorTest extends AbstractInterceptorTest {

    private static final Logger log = LoggerFactory.getLogger(Lec03DeadlineInterceptorTest.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of(new DeadlineInterceptor(Duration.ofSeconds(2)));
    }

    @Test
    public void blockingDeadlineTest(){
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(1)
                    .build();
            var response = this.blockingStub
                           .getAccountBalance(request);
    }


    @Test
    public void testWithdrawDeadline() {
        var observer = ResponseObserver.<Money>create();
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(50)
                .build();

        this.stub.withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                .withdraw(request,observer);

        observer.await();
    }
}

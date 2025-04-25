package org.example.sec09;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.example.common.AbstractChannelTest;
import org.example.common.ResponseObserver;
import org.example.models.sec09.AccountBalance;
import org.example.models.sec09.BalanceCheckRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Lec01UnaryInputValidationTest extends AbstractTest{
    @Test
    public void testBlockingInputValidation() {
        StatusRuntimeException statusRuntimeException = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(11)
                    .build();
            this.blockingStub.getAccountBalance(request);

        });
        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT,statusRuntimeException.getStatus().getCode() );
    }


    @Test
    public void testAsyncInputValidation(){

        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(11).build();

        var observer = ResponseObserver.<AccountBalance>create();
        this.stub.getAccountBalance(request,observer);
        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ((StatusRuntimeException)observer.getThrowable()).getStatus().getCode());


    }
}

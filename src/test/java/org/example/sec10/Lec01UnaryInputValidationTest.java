package org.example.sec10;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import org.example.common.ResponseObserver;
import org.example.models.sec10.AccountBalance;
import org.example.models.sec10.BalanceCheckRequest;
import org.example.models.sec10.ErrorMessage;
import org.example.models.sec10.ValidationCode;
import org.example.sec10.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01UnaryInputValidationTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(Lec01UnaryInputValidationTest.class);

    @Test
    public void testBlockingInputValidation() {
        StatusRuntimeException statusRuntimeException = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(11)
                    .build();
            this.blockingStub.getAccountBalance(request);

        });
        Assertions.assertEquals(ValidationCode.INVALID_ACCOUNT,getValidationCode(statusRuntimeException) );

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
        //Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ((StatusRuntimeException)observer.getThrowable()).getStatus().getCode());
        Assertions.assertEquals(ValidationCode.INVALID_ACCOUNT,getValidationCode(observer.getThrowable()) );


    }
}

package org.example.sec09;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.example.common.ResponseObserver;
import org.example.models.sec09.AccountBalance;
import org.example.models.sec09.BalanceCheckRequest;
import org.example.models.sec09.Money;
import org.example.models.sec09.WithdrawRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class Lec02ServerStreamingInputValidationTest extends AbstractTest{

    @ParameterizedTest
    @MethodSource("testData")
    public void testBlockingInputValidation(WithdrawRequest request, Status.Code code) {
        StatusRuntimeException statusRuntimeException = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            this.blockingStub.withdraw(request).hasNext();
        });
        Assertions.assertEquals(code,statusRuntimeException.getStatus().getCode() );
    }


    @ParameterizedTest
    @MethodSource("testData")
    public void asyncInputValidationTest(WithdrawRequest request, Status.Code code) {
        var observer = ResponseObserver.<Money>create();
        this.stub.withdraw(request,observer);
        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
        Assertions.assertEquals(code, ((StatusRuntimeException)observer.getThrowable()).getStatus().getCode());
    }

    private Stream<Arguments> testData(){
        return Stream.of(
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(10).build(), Status.Code.INVALID_ARGUMENT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(2).setAmount(12).build(), Status.Code.INVALID_ARGUMENT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(10).setAmount(130).build(), Status.Code.FAILED_PRECONDITION)
        );
    }

}

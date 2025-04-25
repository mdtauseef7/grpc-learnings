package org.example.sec10;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.example.common.ResponseObserver;
import org.example.models.sec10.Money;
import org.example.models.sec10.ValidationCode;
import org.example.models.sec10.WithdrawRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class Lec02ServerStreamingInputValidationTest extends AbstractTest {

    @ParameterizedTest
    @MethodSource("testData")
    public void testBlockingInputValidation(WithdrawRequest request, ValidationCode code) {
        StatusRuntimeException statusRuntimeException = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            this.blockingStub.withdraw(request).hasNext();
        });
        Assertions.assertEquals(code,getValidationCode(statusRuntimeException));
    }


    @ParameterizedTest
    @MethodSource("testData")
    public void asyncInputValidationTest(WithdrawRequest request, ValidationCode code) {
        var observer = ResponseObserver.<Money>create();
        this.stub.withdraw(request,observer);
        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
        Assertions.assertEquals(code, getValidationCode(observer.getThrowable()));
    }

    private Stream<Arguments> testData(){
        return Stream.of(
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(10).build(), ValidationCode.INVALID_ACCOUNT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(2).setAmount(12).build(), ValidationCode.INVALID_AMOUNT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(10).setAmount(130).build(), ValidationCode.INSUFFICIENT_BALANCE)
        );
    }
}

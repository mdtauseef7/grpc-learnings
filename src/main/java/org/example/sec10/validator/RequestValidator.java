package org.example.sec10.validator;

import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import org.example.models.sec10.ErrorMessage;
import org.example.models.sec10.ValidationCode;

import java.util.Map;
import java.util.Optional;

public class RequestValidator {

    private static final Metadata.Key<ErrorMessage> ERROR_MESSAGEKEY = ProtoUtils.keyForProto(ErrorMessage.getDefaultInstance());

    public static Optional<StatusRuntimeException> validateAccount(int accountNumber) {
        if (accountNumber > 0 && accountNumber < 11) {
            return Optional.empty();
        }
        return Optional.of(Status.INVALID_ARGUMENT.asRuntimeException(toMetadata(ValidationCode.INVALID_ACCOUNT)));
    }

    public static Optional<StatusRuntimeException> isAmountDivisibleBy10(int amount) {
        if (amount >= 10 && amount % 10 == 0) {
            return Optional.empty();
        }
        return Optional.of(Status.INVALID_ARGUMENT.asRuntimeException(toMetadata(ValidationCode.INVALID_AMOUNT)));
    }


    public static Optional<StatusRuntimeException> hasSufficientBalance(int amount, int balance) {
        if (amount < balance) {
            return Optional.empty();
        }
        return Optional.of(Status.FAILED_PRECONDITION.asRuntimeException(toMetadata(ValidationCode.INSUFFICIENT_BALANCE)));
    }

    private static Metadata toMetadata(ValidationCode code){
        var metadata = new Metadata();
        var errorMessage = ErrorMessage.newBuilder().setValidationCode(code).build();
        metadata.put(ERROR_MESSAGEKEY, errorMessage);
        var key = Metadata.Key.of("desc", Metadata.ASCII_STRING_MARSHALLER);
        metadata.put(key, code.toString());
        return metadata;
    }
}

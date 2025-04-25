package org.example.sec12;

import io.grpc.ClientInterceptor;
import org.example.models.sec12.BalanceCheckRequest;
import org.example.sec12.interceptor.GzipRequestInterceptor;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Lec04CompressionInterceptorTest extends AbstractInterceptorTest{

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of(new GzipRequestInterceptor("gzip"));
    }

    @Test
    public void blockingDeadlineTest(){
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();
        var response = this.blockingStub
                .getAccountBalance(request);
    }

}

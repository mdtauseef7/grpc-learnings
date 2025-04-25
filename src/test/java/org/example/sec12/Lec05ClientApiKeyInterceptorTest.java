package org.example.sec12;

import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import org.example.common.GrpcServer;
import org.example.models.sec12.BalanceCheckRequest;
import org.example.sec12.interceptors.GzipResponseInterceptor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.example.sec12.Constants.API_KEY;

public class Lec05ClientApiKeyInterceptorTest extends AbstractInterceptorTest{

    private static final Logger log = LoggerFactory.getLogger(Lec05ClientApiKeyInterceptorTest.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of(
                MetadataUtils.newAttachHeadersInterceptor(getApiKey())
        );
    }

    protected GrpcServer createServer() {
        return   GrpcServer.create(6565, builder -> {
            builder.addService(new BankService())
                    .intercept(new ApiKeyValidationInterceptor())
                    .build();
        });
    }

    @Test
    public void cleintApiDemo(){

        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1).build();

        var response = this.blockingStub.getAccountBalance(request);
        log.info("{}",response);
    }

    private Metadata getApiKey() {
     var metadata = new Metadata();
         metadata.put(API_KEY, "bank-client-secret");
         return metadata;
    }
}

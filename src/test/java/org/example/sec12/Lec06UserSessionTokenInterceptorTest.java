package org.example.sec12;

import io.grpc.CallCredentials;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import org.example.common.GrpcServer;
import org.example.common.ResponseObserver;
import org.example.models.sec12.BalanceCheckRequest;
import org.example.models.sec12.Money;
import org.example.models.sec12.WithdrawRequest;
import org.example.sec12.interceptors.UserTokenInterceptor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executor;

import static org.example.sec12.Constants.API_KEY;

public class Lec06UserSessionTokenInterceptorTest extends AbstractInterceptorTest{

    private static final Logger log = LoggerFactory.getLogger(Lec06UserSessionTokenInterceptorTest.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of();
    }

    protected GrpcServer createServer() {
        return   GrpcServer.create(6565, builder -> {
            builder.addService(new BankService())
                    .intercept(new UserTokenInterceptor())
                    .build();
        });
    }



    @Test
    public void unaryUserCredentialDemo(){
        for (int i = 1; i <= 5; i++) {
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(1).build();

            var response = this.blockingStub.withCallCredentials(new UsersessionToken("user-token-"+i))
                    .getAccountBalance(request);

            log.info("{}",response);
        }
    }


    @Test
    public void streamingUserCredentialDemo(){
        for (int i = 1; i <= 5; i++) {
         var observer = ResponseObserver.<Money>create();
         var request = WithdrawRequest.newBuilder()
                 .setAccountNumber(i)
                 .setAmount(30)
                 .build();

         this.stub.withCallCredentials(new UsersessionToken("user-token-"+i)).withdraw(request,observer);
         observer.await();
        }
    }

    private static class UsersessionToken extends CallCredentials{
        private static final String TOKEN_FORMAT = "%s %s";
        private final String jwt;

        public UsersessionToken(String jwt) {
            this.jwt = jwt;
        }

        @Override
        public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
            executor.execute(()->{
                var metadata = new Metadata();
                metadata.put(Constants.USER_TOKEN_KEY, TOKEN_FORMAT.formatted(Constants.BEARER,jwt));
                metadataApplier.apply(metadata);
            });

        }
    }
}

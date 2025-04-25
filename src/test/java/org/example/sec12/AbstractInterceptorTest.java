package org.example.sec12;

import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.common.GrpcServer;
import org.example.models.sec12.BankServiceGrpc;
import org.example.sec12.BankService;
import org.example.sec12.interceptors.GzipResponseInterceptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class  AbstractInterceptorTest {
    private GrpcServer grpcServer = GrpcServer.create(6565, builder -> {

        builder.addService(new BankService())
                .intercept(new GzipResponseInterceptor())
                .build();
    });
    protected ManagedChannel channel;
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;
    protected BankServiceGrpc.BankServiceStub stub;

    protected abstract List<ClientInterceptor> getClientInterceptors();


    protected GrpcServer createServer() {
      return   GrpcServer.create(6565, builder -> {
            builder.addService(new BankService())
                    .intercept(new GzipResponseInterceptor())
                    .build();
        });
    }
    @BeforeAll
    public void setup(){
        this.grpcServer = createServer();
        this.grpcServer.start();
        this.channel = ManagedChannelBuilder.forAddress("localhost",6565)
                .usePlaintext()
                .intercept(getClientInterceptors())
                .build();

        this.stub = BankServiceGrpc.newStub(channel);
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);

    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
        this.channel.shutdownNow();
    }
}

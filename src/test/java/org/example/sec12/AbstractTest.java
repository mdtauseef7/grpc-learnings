package org.example.sec12;


import org.example.common.AbstractChannelTest;
import org.example.common.GrpcServer;
import org.example.models.sec12.BankServiceGrpc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.example.sec12.BankService;

public class AbstractTest extends AbstractChannelTest {

    private final GrpcServer grpcServer = GrpcServer.create(new BankService());

    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;
    protected BankServiceGrpc.BankServiceStub stub;


    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
        this.stub = BankServiceGrpc.newStub(channel);

    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }

}

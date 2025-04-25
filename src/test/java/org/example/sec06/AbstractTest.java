package org.example.sec06;

import org.example.common.AbstractChannelTest;
import org.example.common.GrpcServer;
import org.example.models.sec06.BankServiceGrpc;
import org.example.models.sec06.TransferServiceGrpc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class AbstractTest extends AbstractChannelTest {

    private final GrpcServer  grpcServer = GrpcServer.create(new BankService(),new TransferService());

    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;
    protected BankServiceGrpc.BankServiceStub stub;
    protected TransferServiceGrpc.TransferServiceStub transferStub;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
        this.stub = BankServiceGrpc.newStub(channel);
        this.transferStub = TransferServiceGrpc.newStub(channel);
    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }
}

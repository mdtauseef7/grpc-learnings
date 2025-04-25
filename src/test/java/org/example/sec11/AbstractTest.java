package org.example.sec11;


import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import org.example.common.AbstractChannelTest;
import org.example.common.GrpcServer;
import org.example.models.sec11.BankServiceGrpc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.Optional;

public class AbstractTest extends AbstractChannelTest {

    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());

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

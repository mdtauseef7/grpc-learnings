package org.example.sec07;

import org.example.common.AbstractChannelTest;
import org.example.common.GrpcServer;
import org.example.models.sec07.FlowControlServiceGrpc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlowControlTest extends AbstractChannelTest {

    private final GrpcServer server= GrpcServer.create(new FlowControlService());
    private FlowControlServiceGrpc.FlowControlServiceStub stub;

    @BeforeAll
    public void setUp(){
        this.server.start();
        this.stub = FlowControlServiceGrpc.newStub(channel);
    }

    @Test
    public void flowControl(){
        var responseObserver = new ResponseHandler();
        var requestObserver = this.stub.getMessages(responseObserver);
        responseObserver.setRequestObserver(requestObserver);
        responseObserver.start();
        responseObserver.await();
    }

    @AfterAll
    public void stop(){
        this.server.stop();
    }

}

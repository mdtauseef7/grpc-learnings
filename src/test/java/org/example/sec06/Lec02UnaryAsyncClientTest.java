package org.example.sec06;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.example.common.ResponseObserver;
import org.example.models.sec06.AccountBalance;
import org.example.models.sec06.AccountBalances;
import org.example.models.sec06.BalanceCheckRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec02UnaryAsyncClientTest extends AbstractTest{


    private static final Logger log = LoggerFactory.getLogger(Lec02UnaryAsyncClientTest.class);

    @Test
    public void getBalanceTest(){
        var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        var observer = ResponseObserver.<AccountBalance>create();
        this.stub.getAccountBalance(request, observer );

        observer.await();

        Assertions.assertEquals(1,observer.getItems().size());
        Assertions.assertEquals(100, observer.getItems().getFirst().getBalance());
        Assertions.assertNull(observer.getThrowable());

    }


    @Test
    public void getAllAccountTest(){
        var observer = ResponseObserver.<AccountBalances>create();
        this.stub.getAllAccounts(Empty.newBuilder().build(),observer);

        observer.await();
        Assertions.assertEquals(1,observer.getItems().size());
        Assertions.assertEquals(10, observer.getItems().getFirst().getAccountsCount());
        Assertions.assertNull(observer.getThrowable());

    }

}

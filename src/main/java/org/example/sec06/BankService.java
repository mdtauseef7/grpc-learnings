package org.example.sec06;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.example.models.sec06.*;
import org.example.sec06.repository.AccountRepository;
import org.example.sec06.requesthandlers.DepositRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BankService.class);

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        var accountNumber = request.getAccountNumber();
        var balance = AccountRepository.getBalance(accountNumber);
        var accountBalance = AccountBalance.newBuilder().setAccountNumber(accountNumber)
                .setBalance(balance)
                .build();

        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllAccounts(Empty request, StreamObserver<AccountBalances> responseObserver) {

        List<AccountBalance> list = AccountRepository.getAllAccounts().entrySet()
                .stream()
                .map(e -> AccountBalance.newBuilder().setAccountNumber(e.getKey()).setBalance(e.getValue()).build())
                .toList();

        AccountBalances allAccounts = AccountBalances.newBuilder().addAllAccounts(list).build();

        responseObserver.onNext(allAccounts);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
         //Input validation

        var accountNumber = request.getAccountNumber();
        var requestedAmount = request.getAmount();
        var accountBalance = AccountRepository.getBalance(accountNumber);

        if(requestedAmount > accountBalance){
            responseObserver.onCompleted();
            return;
        }

        for(int i=0; i< (requestedAmount/10); i++){
            var money = Money.newBuilder().setAmount(10).build();
            responseObserver.onNext(money);
            log.info("money sent {}", money);

            AccountRepository.deductBalance(accountNumber,10);
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<DepositRequest> deposit(StreamObserver<AccountBalance> responseObserver) {
        return new DepositRequestHandler(responseObserver);
    }
}

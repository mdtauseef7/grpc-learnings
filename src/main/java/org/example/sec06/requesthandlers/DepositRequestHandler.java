package org.example.sec06.requesthandlers;

import io.grpc.stub.StreamObserver;
import org.example.models.sec06.AccountBalance;
import org.example.models.sec06.DepositRequest;
import org.example.sec06.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DepositRequestHandler implements StreamObserver<DepositRequest> {
    private static final Logger log = LoggerFactory.getLogger(DepositRequestHandler.class);
    private final StreamObserver<AccountBalance> responseObserver;
    private int accountNumber;



    public DepositRequestHandler(StreamObserver<AccountBalance> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onCompleted() {
            var accountBalance = AccountBalance.newBuilder().setAccountNumber(this.accountNumber)
                    .setBalance(AccountRepository.getBalance(this.accountNumber))
                    .build();
            this.responseObserver.onNext(accountBalance);
            this.responseObserver.onCompleted();
    }

    @Override
    public void onNext(DepositRequest depositRequest) {
        switch (depositRequest.getRequestCase()){
            case ACCOUNT_NUMBER ->  this.accountNumber = depositRequest.getAccountNumber();
            case MONEY -> AccountRepository.depositBalance(this.accountNumber, depositRequest.getMoney().getAmount());
        }

    }

    @Override
    public void onError(Throwable throwable) {
       log.info("client error: {}",throwable.getMessage());
    }
}

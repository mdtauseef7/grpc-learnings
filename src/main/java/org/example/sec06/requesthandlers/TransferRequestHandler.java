package org.example.sec06.requesthandlers;

import io.grpc.stub.StreamObserver;
import org.example.models.sec06.AccountBalance;
import org.example.models.sec06.TransferRequest;
import org.example.models.sec06.TransferResponse;
import org.example.models.sec06.TransferStatus;
import org.example.sec06.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferRequestHandler implements StreamObserver<TransferRequest> {

    private static final Logger log = LoggerFactory.getLogger(TransferRequestHandler.class);
    private final StreamObserver<TransferResponse> responseSObserver;

    public TransferRequestHandler(StreamObserver<TransferResponse> responseSObserver) {
        this.responseSObserver = responseSObserver;
    }

    @Override
    public void onCompleted() {
        log.info("transfer request stream completed");
        this.responseSObserver.onCompleted();
    }

    @Override
    public void onNext(TransferRequest transferRequest) {
        var status = this.transfer(transferRequest);
        var response = TransferResponse.newBuilder()
                .setFromAccount(this.toAccountBalance(transferRequest.getFromAccount()))
                .setToAccount(this.toAccountBalance(transferRequest.getToAccount()))
                .setStatus(status).build();

        this.responseSObserver.onNext(response);
    }

    @Override
    public void onError(Throwable throwable) {
        log.info("client error: {}",throwable.getMessage());
    }

    private TransferStatus transfer(TransferRequest request){
        var amount = request.getAmount();
        var fromAccount = request.getFromAccount();
        var toAccount = request.getToAccount();
         var staus = TransferStatus.REJECTED;
        if(AccountRepository.getBalance(fromAccount) >= amount && (fromAccount != toAccount)){
            AccountRepository.deductBalance(fromAccount,amount);
            AccountRepository.depositBalance(toAccount, amount);
            staus = TransferStatus.COMPLETED;
        }
        return staus;
    }

    private AccountBalance toAccountBalance(int accountNumber){
      return AccountBalance.newBuilder().setAccountNumber(accountNumber)
                .setBalance(AccountRepository.getBalance(accountNumber))
              .build();

    }
}

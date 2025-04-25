package org.example.common;


import org.example.sec12.ApiKeyValidationInterceptor;
import org.example.sec12.BankService;

public class Demo {
    public static void main(String[] args) {
           GrpcServer.create(6565, builder -> {
            builder.addService(new BankService())
                    .intercept(new ApiKeyValidationInterceptor())
                    .build();
        }).start().await();

        /*GrpcServer.create(new BankService())
                .start()
                .await();*/

    }
}

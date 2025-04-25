package org.example.sec12;

import io.grpc.*;

import java.util.Objects;

import static org.example.sec12.Constants.API_KEY;

public class ApiKeyValidationInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler) {
        var  apiKey = metadata.get(API_KEY);
        if(isValid(apiKey)){
            return serverCallHandler.startCall(serverCall,metadata);
        }
        serverCall.close(Status.UNAUTHENTICATED.withDescription("Client must provide valid api key"),metadata);
        return new ServerCall.Listener<ReqT>() {};
    }

    private boolean isValid(String apiKey){
        return "bank-client-secret".equals(apiKey);
    }
}

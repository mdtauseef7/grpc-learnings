package org.example.sec12.interceptor;

import io.grpc.*;

import java.util.Objects;

public class GzipRequestInterceptor implements ClientInterceptor {

    private final String compressionAlg;

    public GzipRequestInterceptor(String compressionAlg) {
        this.compressionAlg = compressionAlg;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        callOptions = Objects.nonNull(callOptions.getCompressor())?
                callOptions:
                callOptions.withCompression(compressionAlg);
        return channel.newCall(methodDescriptor,callOptions);
    }
}

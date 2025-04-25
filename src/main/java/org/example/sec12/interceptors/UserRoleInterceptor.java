package org.example.sec12.interceptors;

import io.grpc.*;
import org.example.sec12.Constants;
import org.example.sec12.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

public class UserRoleInterceptor implements ServerInterceptor {
        private static final Logger log = LoggerFactory.getLogger(UserRoleInterceptor.class);
        public static final Set<String> PRIME_SET = Set.of("user-token-1","user-token-2");
        public static final Set<String> STANDARD_SET = Set.of("user-token-3","user-token-4");

        @Override
        public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
            Metadata metadata,
            ServerCallHandler<ReqT, RespT> serverCallHandler) {
        var token = extractToken(metadata.get(Constants.USER_TOKEN_KEY));
        log.info("Token: {}", token);
        var ctx = toContext(token);
        if(Objects.nonNull(ctx)){
          return Contexts.interceptCall(ctx, serverCall,metadata,serverCallHandler);
        }
        return close(serverCall, metadata, Status.UNAUTHENTICATED.withDescription("Token is either null or invalid"));
    }

        private String extractToken(String value){
        return Objects.nonNull(value) && value.contains(Constants.BEARER)?
                value.substring(Constants.BEARER.length()).strip() : null;

    }

        private Context toContext(String token){
              if(Objects.nonNull(token) && (PRIME_SET.contains(token) || STANDARD_SET.contains(token))){
                    var role = PRIME_SET.contains(token) ? UserRole.PRIME : UserRole.STANDARD;
                  return Context.current().withValue(Constants.USER_ROLE_KEY, role);
                }
            return null;
        }

        private <ReqT, ResT> ServerCall.Listener<ReqT> close(ServerCall<ReqT, ResT> serverCall, Metadata metadata, Status status){
        serverCall.close(status,metadata);
        return new ServerCall.Listener<ReqT>() {  };
    }
}

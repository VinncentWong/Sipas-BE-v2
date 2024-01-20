package org.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.example.constant.ContextConstant;
import org.example.constant.HttpHeaderConstant;
import org.example.jwt.JwtUtil;
import org.example.response.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

@Component
@Slf4j
@Order(1)
public class JwtFilter implements GlobalFilter {

    @Autowired
    private ObjectMapper mapper;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final List<String> nonSecuredEndpoints = List.of(
            "/parent/register",
            "/parent/login",
            "/check"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var response = exchange.getResponse();
        response.getHeaders()
                .put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_JSON_VALUE));
        Predicate<ServerHttpRequest> requestPredicate = (r) -> {
            var path = r
                    .getURI()
                    .getPath();
            log.info("catch path: {}", path);
            return nonSecuredEndpoints
                    .stream()
                    .anyMatch(path::contains);
        };

        if (requestPredicate.test(request)) {
            return chain
                    .filter(exchange)
                    .then(Mono.fromRunnable(logResponse(exchange)));
        } else {
            try {
                var header = request.getHeaders()
                        .getOrEmpty(HttpHeaderConstant.AUTHORIZATION);
                if (!header.isEmpty()) {
                    var token = header.get(0)
                            .substring(7);
                    if(StringUtils.isBlank(token) || !token.startsWith("Bearer")){
                        var httpResponse = HttpResponse
                                .sendErrorResponse(
                                        "format token tidak valid!"
                                );
                        response.setStatusCode(HttpStatus.FORBIDDEN);
                        return response
                                .writeWith(
                                        this.getDataBuffer(response, httpResponse)
                                );
                    } else {
                        var jwtToken = JwtUtil.getTokenData(this.jwtSecret, token);
                        request.getHeaders().put(
                                HttpHeaderConstant.USER_ID,
                                List.of(String.valueOf(jwtToken.getId()))
                        );
                        return chain
                                .filter(exchange)
                                .then(Mono.fromRunnable(logResponse(exchange)));
                    }
                } else {
                    var httpResponse = HttpResponse
                            .sendErrorResponse(
                                    "header kosong! silahkan masukkan header sesuai ketentuan yang sudah ada"
                            );
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    return response
                            .writeWith(
                                    this.getDataBuffer(response, httpResponse)
                            );
                }
            } catch (Exception e) {
                var httpResponse = HttpResponse
                        .sendErrorResponse(
                                String.format("jwt token tidak valid: %s", e.getMessage())
                        );
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response
                        .writeWith(
                                getDataBuffer(response, httpResponse)
                        );
            }
        }
    }

    @SneakyThrows
    private Mono<DataBuffer> getDataBuffer(ServerHttpResponse response, Object httpResponse){
        return Mono.just(
                response.bufferFactory()
                        .wrap(
                                this.mapper
                                        .writeValueAsBytes(httpResponse)
                        )
        );
    }

    private Runnable logResponse(ServerWebExchange exchange){
        return () -> {
            var postResponse = exchange.getResponse();
            var directedRoute = exchange.<URI>getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
            log.info("get response: status-code-> {} from URI -> {}", postResponse.getStatusCode(), directedRoute);
        };
    }
}

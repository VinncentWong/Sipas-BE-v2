package org.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.micrometer.common.util.StringUtils;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.example.constant.GatewayConstant;
import org.example.jwt.JwtUtil;
import org.example.response.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.function.Predicate;

@Component
public class JwtFilter implements WebFilter {

    @Autowired
    private ObjectMapper mapper;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private List<String> nonSecuredEndpoints = List.of(
            "/auth/register/parent",
            "/auth/login/parent"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var currentTime = LocalDateTime.now();
        var request = exchange.getRequest();
        var response = exchange.getResponse();
        Predicate<ServerHttpRequest> requestPredicate = (r) -> {
            var path = r
                    .getURI()
                    .getPath();
            return nonSecuredEndpoints
                    .stream()
                    .anyMatch(path::equals);
        };

        if (requestPredicate.test(request)) {
            return chain
                    .filter(exchange)
                    .contextWrite((c) ->
                            c
                                    .put(GatewayConstant.TIME_START, currentTime)
                                    .put(GatewayConstant.REQUEST_PATH, request.getURI().getPath())

                    );
        } else {
            var context = Context.of(GatewayConstant.TIME_START, currentTime);
            try {
                var header = request.getHeaders()
                        .getOrEmpty(HttpHeaders.AUTHORIZATION);
                if (!header.isEmpty()) {
                    var token = header.get(0)
                            .substring(7);
                    if(StringUtils.isBlank(token) || !token.startsWith("Bearer")){
                        var httpResponse = HttpResponse
                                .sendErrorResponse(
                                        context,
                                        HttpStatus.FORBIDDEN,
                                        "format token tidak valid!"
                                );
                        return response
                                .writeWith(
                                        this.getDataBuffer(response, httpResponse)
                                );
                    } else {
                        var jwtToken = JwtUtil.getTokenData(this.jwtSecret, token);
                        return chain
                                .filter(exchange)
                                .contextWrite((c) ->
                                        c
                                                .put(GatewayConstant.TIME_START, currentTime)
                                                .put(GatewayConstant.REQUEST_PATH, request.getURI().getPath())
                                                .put(GatewayConstant.JWT_DATA, jwtToken)

                                );
                    }
                } else {
                    var httpResponse = HttpResponse
                            .sendErrorResponse(
                                    context,
                                    HttpStatus.FORBIDDEN,
                                    "header kosong! silahkan masukkan header sesuai ketentuan yang sudah ada"
                            );
                    return response
                            .writeWith(
                                    this.getDataBuffer(response, httpResponse)
                            );
                }
            } catch (Exception e) {
                var httpResponse = HttpResponse
                        .sendErrorResponse(
                                context,
                                HttpStatus.FORBIDDEN,
                                String.format("jwt token tidak valid: %s", e.getMessage())
                        );
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
}

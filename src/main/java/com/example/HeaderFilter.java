package com.example;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class HeaderFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var request = exchange.getRequest();
        var response = exchange.getResponse();

        String acceptHeader = request.getHeaders().getFirst("Accept");
        String contentType = request.getHeaders().getFirst("Content-Type");
        HttpMethod method = request.getMethod();

        boolean isPostOrPut = HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method);

        if (!MediaType.APPLICATION_JSON_VALUE.equals(acceptHeader)) {
            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return response.setComplete();
        }

        if (isPostOrPut && !MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            response.setStatusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }
}
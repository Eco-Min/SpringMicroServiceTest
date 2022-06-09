package com.example._apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter() {
        super(LoggingFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        GatewayFilter orderedGatewayFilter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("LOGGING PRE filter : request id -> {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("LOGGING PRE filter : request getId -> {}", request.getId());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPreLogger()) {
                    log.info("LOGGING POST filter : request getId -> {}", response.getStatusCode());
                }
            }));
        }, Ordered.HIGHEST_PRECEDENCE);
        // order 부분이 filter의 순서를 정할 수 있다.
        return orderedGatewayFilter;
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean PostLogger;
    }
}
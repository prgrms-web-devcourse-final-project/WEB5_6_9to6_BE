package com.grepp.spring.infra.config.Chat.WebSocket;


import com.grepp.spring.infra.config.Chat.WebSocket.Auth.WebSocketAuthInterceptor;
import java.security.Principal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/subscribe", "/queue");
        config.setApplicationDestinationPrefixes("/publish");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // websocket handshake endpoint
        registry.addEndpoint("/ws-connect")

            //TODO 실 서비스 시 도메인 확인해서 잘 연결하기
//            .setAllowedOrigins("http://localhost:8080",
//                "http://127.0.0.1:5500",
//                "http://localhost:3000",
//                "http://3.37.19.66",
//                "https://3.37.19.66",
//                "https://www.stuidium.com",
//                "http://www.stuidium.com")
//
            .setAllowedOriginPatterns("*") // 개발용 루트 열기
            .setHandshakeHandler(new DefaultHandshakeHandler() {
                @Override
                protected Principal determineUser(ServerHttpRequest request,
                    WebSocketHandler wsHandler, Map<String, Object> attributes) {
                    Authentication authentication = SecurityContextHolder.getContext()
                        .getAuthentication();
                    return (authentication != null && authentication.isAuthenticated())
                        ? authentication : null;
                }
            })
            .withSockJS(); // SockJS fallback 지원;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketAuthInterceptor);
    }

}

package com.grepp.spring.infra.config.Chat;


import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;


// redis pub/sub 기반 구현
@Configuration
@RequiredArgsConstructor
public class RedisSubscriberConfig {

//
//    private final RedisMessageListenerContainer listenerContainer;
//    private final ChatSubscriber chatSubscriber;
//
//
//
//    @PostConstruct
//    public void subscribe(){
//        listenerContainer.addMessageListener(chatSubscriber, new PatternTopic("chat:*"));
//    }
//
//


}

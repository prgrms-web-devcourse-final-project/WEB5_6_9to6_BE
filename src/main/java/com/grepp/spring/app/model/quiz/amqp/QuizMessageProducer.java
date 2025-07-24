package com.grepp.spring.app.model.quiz.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "quiz.exchange";
    private static final String ROUTING_KEY = "quiz.creation.key";

    public void sendCreateNextQuizRequest(Long studyId) {
        CreateNextQuizMessage message = new CreateNextQuizMessage(studyId);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
    }
}
package com.grepp.spring.app.model.quiz.amqp;

import com.grepp.spring.app.model.quiz.service.QuizCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuizMessageConsumer {

    private final QuizCreateService quizCreateService;

    @RabbitListener(queues = "quiz.creation.queue")
    public void receiveCreateNextQuizRequest(CreateNextQuizMessage message) {
        Long studyId = message.getStudyId();
        log.info("Received 'create next quiz' request for studyId: {}", studyId);
        try {
            quizCreateService.createNextQuiz(studyId);
        } catch (Exception e) {
            log.error("[RabbitMQ] Failed to create next quiz for studyId {}: {}", studyId, e.getMessage());
        }
    }
}
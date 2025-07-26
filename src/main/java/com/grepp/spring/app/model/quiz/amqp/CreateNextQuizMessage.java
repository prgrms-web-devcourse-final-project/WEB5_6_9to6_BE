package com.grepp.spring.app.model.quiz.amqp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateNextQuizMessage {
    private Long studyId;
}
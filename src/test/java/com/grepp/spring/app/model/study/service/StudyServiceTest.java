package com.grepp.spring.app.model.study.service;

import com.grepp.spring.app.model.study.dto.response.GoalsResponse;
import com.grepp.spring.app.model.study.repository.goal.StudyGoalRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock
    private StudyGoalRepository studyGoalRepository;

    @InjectMocks
    private StudyService studyService;

    @Test
    @DisplayName("find all goals in one study")
    void findGoalsByIdTest() {
        // given
        Long studyId = 1L;

        List<GoalsResponse> expectedGoals = Arrays.asList(
            new GoalsResponse(1L, "미국대사관 직원이랑 영어 랩배틀"),
            new GoalsResponse(2L, "백악관 전화 해킹해서 트럼프에게 문안인사드리기"),
            new GoalsResponse(3L, "자주쓰는 원어민 표현 100개 외우기")
        );

        when(studyGoalRepository.findStudyGoals(studyId)).thenReturn(expectedGoals);

        List<GoalsResponse> actualResponse = studyService.findGoals(studyId);

        assertEquals(expectedGoals, actualResponse);
        verify(studyGoalRepository, times(1)).findStudyGoals(studyId);
    }

}
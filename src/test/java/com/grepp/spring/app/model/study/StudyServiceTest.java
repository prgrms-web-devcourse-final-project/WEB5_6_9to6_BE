package com.grepp.spring.app.model.study;


import com.grepp.spring.app.model.member.dto.response.ApplicantsResponse;
import com.grepp.spring.app.model.study.code.ApplicantState;
import com.grepp.spring.app.model.study.repository.StudyRepository;
import com.grepp.spring.app.model.study.service.StudyService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock // Mock 객체로 만들 Repository
    private StudyRepository studyRepository;

    @InjectMocks // @Mock 객체를 주입받을 Service 객체
    private StudyService studyService;

    @Test
    @DisplayName("Success Search - applicant list")
    void getApplicants_success() {
        // given
        Long studyId = 1L;

        // 가짜 데이터 리스트 생성
        List<ApplicantsResponse> expectedResponses = Arrays.asList(
            new ApplicantsResponse(101L, 201L, "test1", ApplicantState.WAIT, "I'm ready to study hard!"),
            new ApplicantsResponse(102L, 202L, "test2", ApplicantState.ACCEPT, "Hi, I want to join here.")
        );

        // studyRepository.findAllApplicants(studyId) 메서드가 호출되면, expectedResponses 리스트를 반환
        when(studyRepository.findAllApplicants(studyId)).thenReturn(expectedResponses);

        // when
        List<ApplicantsResponse> actualResponses = studyService.getApplicants(studyId);
//        System.out.println(actualResponses.toString());

        // then (결과 검증)
        assertThat(actualResponses).isNotNull();
        assertThat(actualResponses.size()).isEqualTo(2);
        assertThat(actualResponses.get(0).getName()).isEqualTo("test1");
        assertThat(actualResponses.get(1).getState()).isEqualTo(ApplicantState.ACCEPT);

        // studyRepository.findAllApplicants(studyId)가 정확히 1번 호출되었는지 검증
        verify(studyRepository, times(1)).findAllApplicants(studyId);
    }



}
package com.grepp.spring.app.model.timer.service;

import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.timer.repository.TimerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimerService {

    private final TimerRepository timerRepository;
    private final StudyMemberRepository studyMemberRepository;

    public Long getAllStudyTime(Long memberId) {
        List<Long> studyMemberIds = studyMemberRepository.findAllStudies(memberId);

        if (studyMemberIds == null || studyMemberIds.isEmpty()) {
            log.info("studyMemberIds is null or empty");
            return 0L;
        }
        log.info("studyMemberIds: {}", studyMemberIds);
        Long totalStudyTime = timerRepository.findTotalStudyTimeByStudyMemberIds(studyMemberIds);

        return totalStudyTime != null ? totalStudyTime : 0L;
    }

    public void recordStudyTime(Long studyId, Long studyMemberId) {

    }
}

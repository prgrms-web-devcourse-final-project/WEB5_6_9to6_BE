package com.grepp.studium.timer.service;

import com.grepp.studium.study_member.domain.StudyMember;
import com.grepp.studium.study_member.repos.StudyMemberRepository;
import com.grepp.studium.timer.domain.Timer;
import com.grepp.studium.timer.model.TimerDTO;
import com.grepp.studium.timer.repos.TimerRepository;
import com.grepp.studium.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TimerService {

    private final TimerRepository timerRepository;
    private final StudyMemberRepository studyMemberRepository;

    public TimerService(final TimerRepository timerRepository,
            final StudyMemberRepository studyMemberRepository) {
        this.timerRepository = timerRepository;
        this.studyMemberRepository = studyMemberRepository;
    }

    public List<TimerDTO> findAll() {
        final List<Timer> timers = timerRepository.findAll(Sort.by("timerId"));
        return timers.stream()
                .map(timer -> mapToDTO(timer, new TimerDTO()))
                .toList();
    }

    public TimerDTO get(final Integer timerId) {
        return timerRepository.findById(timerId)
                .map(timer -> mapToDTO(timer, new TimerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TimerDTO timerDTO) {
        final Timer timer = new Timer();
        mapToEntity(timerDTO, timer);
        return timerRepository.save(timer).getTimerId();
    }

    public void update(final Integer timerId, final TimerDTO timerDTO) {
        final Timer timer = timerRepository.findById(timerId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(timerDTO, timer);
        timerRepository.save(timer);
    }

    public void delete(final Integer timerId) {
        timerRepository.deleteById(timerId);
    }

    private TimerDTO mapToDTO(final Timer timer, final TimerDTO timerDTO) {
        timerDTO.setTimerId(timer.getTimerId());
        timerDTO.setDailyStudyTime(timer.getDailyStudyTime());
        timerDTO.setCreatedAt(timer.getCreatedAt());
        timerDTO.setStudyMember(timer.getStudyMember() == null ? null : timer.getStudyMember().getStudyMemberId());
        return timerDTO;
    }

    private Timer mapToEntity(final TimerDTO timerDTO, final Timer timer) {
        timer.setDailyStudyTime(timerDTO.getDailyStudyTime());
        timer.setCreatedAt(timerDTO.getCreatedAt());
        final StudyMember studyMember = timerDTO.getStudyMember() == null ? null : studyMemberRepository.findById(timerDTO.getStudyMember())
                .orElseThrow(() -> new NotFoundException("studyMember not found"));
        timer.setStudyMember(studyMember);
        return timer;
    }

}

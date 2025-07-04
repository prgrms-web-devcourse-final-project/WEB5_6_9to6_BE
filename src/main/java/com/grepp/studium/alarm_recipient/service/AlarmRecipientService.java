package com.grepp.studium.alarm_recipient.service;

import com.grepp.studium.alarm.domain.Alarm;
import com.grepp.studium.alarm.repos.AlarmRepository;
import com.grepp.studium.alarm_recipient.domain.AlarmRecipient;
import com.grepp.studium.alarm_recipient.model.AlarmRecipientDTO;
import com.grepp.studium.alarm_recipient.repos.AlarmRecipientRepository;
import com.grepp.studium.member.domain.Member;
import com.grepp.studium.member.repos.MemberRepository;
import com.grepp.studium.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AlarmRecipientService {

    private final AlarmRecipientRepository alarmRecipientRepository;
    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    public AlarmRecipientService(final AlarmRecipientRepository alarmRecipientRepository,
            final AlarmRepository alarmRepository, final MemberRepository memberRepository) {
        this.alarmRecipientRepository = alarmRecipientRepository;
        this.alarmRepository = alarmRepository;
        this.memberRepository = memberRepository;
    }

    public List<AlarmRecipientDTO> findAll() {
        final List<AlarmRecipient> alarmRecipients = alarmRecipientRepository.findAll(Sort.by("alarmRecipientId"));
        return alarmRecipients.stream()
                .map(alarmRecipient -> mapToDTO(alarmRecipient, new AlarmRecipientDTO()))
                .toList();
    }

    public AlarmRecipientDTO get(final Integer alarmRecipientId) {
        return alarmRecipientRepository.findById(alarmRecipientId)
                .map(alarmRecipient -> mapToDTO(alarmRecipient, new AlarmRecipientDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AlarmRecipientDTO alarmRecipientDTO) {
        final AlarmRecipient alarmRecipient = new AlarmRecipient();
        mapToEntity(alarmRecipientDTO, alarmRecipient);
        return alarmRecipientRepository.save(alarmRecipient).getAlarmRecipientId();
    }

    public void update(final Integer alarmRecipientId, final AlarmRecipientDTO alarmRecipientDTO) {
        final AlarmRecipient alarmRecipient = alarmRecipientRepository.findById(alarmRecipientId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(alarmRecipientDTO, alarmRecipient);
        alarmRecipientRepository.save(alarmRecipient);
    }

    public void delete(final Integer alarmRecipientId) {
        alarmRecipientRepository.deleteById(alarmRecipientId);
    }

    private AlarmRecipientDTO mapToDTO(final AlarmRecipient alarmRecipient,
            final AlarmRecipientDTO alarmRecipientDTO) {
        alarmRecipientDTO.setAlarmRecipientId(alarmRecipient.getAlarmRecipientId());
        alarmRecipientDTO.setIsRead(alarmRecipient.getIsRead());
        alarmRecipientDTO.setActivated(alarmRecipient.getActivated());
        alarmRecipientDTO.setAlarm(alarmRecipient.getAlarm() == null ? null : alarmRecipient.getAlarm().getAlarmId());
        alarmRecipientDTO.setMember(alarmRecipient.getMember() == null ? null : alarmRecipient.getMember().getMemberId());
        return alarmRecipientDTO;
    }

    private AlarmRecipient mapToEntity(final AlarmRecipientDTO alarmRecipientDTO,
            final AlarmRecipient alarmRecipient) {
        alarmRecipient.setIsRead(alarmRecipientDTO.getIsRead());
        alarmRecipient.setActivated(alarmRecipientDTO.getActivated());
        final Alarm alarm = alarmRecipientDTO.getAlarm() == null ? null : alarmRepository.findById(alarmRecipientDTO.getAlarm())
                .orElseThrow(() -> new NotFoundException("alarm not found"));
        alarmRecipient.setAlarm(alarm);
        final Member member = alarmRecipientDTO.getMember() == null ? null : memberRepository.findById(alarmRecipientDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        alarmRecipient.setMember(member);
        return alarmRecipient;
    }

}

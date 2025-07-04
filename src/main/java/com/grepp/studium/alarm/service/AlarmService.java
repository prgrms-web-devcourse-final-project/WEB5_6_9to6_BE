package com.grepp.studium.alarm.service;

import com.grepp.studium.alarm.domain.Alarm;
import com.grepp.studium.alarm.model.AlarmDTO;
import com.grepp.studium.alarm.repos.AlarmRepository;
import com.grepp.studium.alarm_recipient.domain.AlarmRecipient;
import com.grepp.studium.alarm_recipient.repos.AlarmRecipientRepository;
import com.grepp.studium.util.NotFoundException;
import com.grepp.studium.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final AlarmRecipientRepository alarmRecipientRepository;

    public AlarmService(final AlarmRepository alarmRepository,
            final AlarmRecipientRepository alarmRecipientRepository) {
        this.alarmRepository = alarmRepository;
        this.alarmRecipientRepository = alarmRecipientRepository;
    }

    public List<AlarmDTO> findAll() {
        final List<Alarm> alarms = alarmRepository.findAll(Sort.by("alarmId"));
        return alarms.stream()
                .map(alarm -> mapToDTO(alarm, new AlarmDTO()))
                .toList();
    }

    public AlarmDTO get(final Integer alarmId) {
        return alarmRepository.findById(alarmId)
                .map(alarm -> mapToDTO(alarm, new AlarmDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AlarmDTO alarmDTO) {
        final Alarm alarm = new Alarm();
        mapToEntity(alarmDTO, alarm);
        return alarmRepository.save(alarm).getAlarmId();
    }

    public void update(final Integer alarmId, final AlarmDTO alarmDTO) {
        final Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(alarmDTO, alarm);
        alarmRepository.save(alarm);
    }

    public void delete(final Integer alarmId) {
        alarmRepository.deleteById(alarmId);
    }

    private AlarmDTO mapToDTO(final Alarm alarm, final AlarmDTO alarmDTO) {
        alarmDTO.setAlarmId(alarm.getAlarmId());
        alarmDTO.setReceiver(alarm.getReceiver());
        alarmDTO.setSender(alarm.getSender());
        alarmDTO.setMessage(alarm.getMessage());
        alarmDTO.setType(alarm.getType());
        alarmDTO.setActivated(alarm.getActivated());
        alarmDTO.setCreatedAt(alarm.getCreatedAt());
        return alarmDTO;
    }

    private Alarm mapToEntity(final AlarmDTO alarmDTO, final Alarm alarm) {
        alarm.setReceiver(alarmDTO.getReceiver());
        alarm.setSender(alarmDTO.getSender());
        alarm.setMessage(alarmDTO.getMessage());
        alarm.setType(alarmDTO.getType());
        alarm.setActivated(alarmDTO.getActivated());
        alarm.setCreatedAt(alarmDTO.getCreatedAt());
        return alarm;
    }

    public ReferencedWarning getReferencedWarning(final Integer alarmId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(NotFoundException::new);
        final AlarmRecipient alarmAlarmRecipient = alarmRecipientRepository.findFirstByAlarm(alarm);
        if (alarmAlarmRecipient != null) {
            referencedWarning.setKey("alarm.alarmRecipient.alarm.referenced");
            referencedWarning.addParam(alarmAlarmRecipient.getAlarmRecipientId());
            return referencedWarning;
        }
        return null;
    }

}

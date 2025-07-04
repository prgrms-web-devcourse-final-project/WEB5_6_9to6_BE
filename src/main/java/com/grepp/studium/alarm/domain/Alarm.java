package com.grepp.studium.alarm.domain;

import com.grepp.studium.alarm_recipient.domain.AlarmRecipient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Alarms")
@Getter
@Setter
public class Alarm {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer alarmId;

    @Column(nullable = false)
    private String receiver;

    @Column
    private String sender;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Boolean activated;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "alarm")
    private Set<AlarmRecipient> alarmAlarmRecipients = new HashSet<>();

}

package com.grepp.spring.infra.mail;

import com.grepp.spring.infra.error.exceptions.MailSendFailureException;
import com.grepp.spring.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromAddress;

    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();

        try {
            message.setTo(to);
            message.setFrom(fromAddress);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            log.info("Mail sent to {}", to);
        } catch (Exception e) {
            log.error("Mail sent to {} failed", to, e);
            throw new MailSendFailureException(ResponseCode.MAIL_SEND_FAIL, e);
        }
    }


}

package com.grepp.spring.infra.config.Chat.WebSocket.Auth;

import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.study.service.StudyService;
import com.grepp.spring.infra.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final StudyService studyService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        Authentication authentication = (Authentication) accessor.getUser();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("인증되지 않은 사용자입니다.");
        }

        com.grepp.spring.app.model.auth.domain.Principal principal =
            (com.grepp.spring.app.model.auth.domain.Principal) authentication.getPrincipal();

        Long memberId = principal.getMemberId();
        StompCommand command = accessor.getCommand();
        String destination = accessor.getDestination();

        // system destination 은 건너뜀
        if (destination == null ||
            (!destination.startsWith("/publish/chat") && !destination.startsWith("/subscribe/chat"))) {
            return message; // 검사 안 함
        }


        // 구독 요청일 때
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            Long studyId = extractStudyId(accessor.getDestination());
            if (!studyService.isUserStudyMember(memberId, studyId)) {
                log.warn("WebSocket access denied: memberId={} studyId={} command={}", memberId, studyId, accessor.getCommand());
                throw new AccessDeniedException("스터디 멤버만 구독할 수 있습니다.");
            }
        }

        // 메시지 보낼 때
        if (StompCommand.SEND.equals(accessor.getCommand())) {
            Long studyId = extractStudyId(accessor.getDestination());
            if (!studyService.isUserStudyMember(memberId, studyId)) {
                log.warn("WebSocket access denied: memberId={} studyId={} command={}", memberId, studyId, accessor.getCommand());
                throw new AccessDeniedException("스터디 멤버만 메시지를 보낼 수 있습니다.");
            }
        }

        // CONNECT 시 studyId 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String studyIdHeader = accessor.getFirstNativeHeader("studyId");

            if (studyIdHeader != null) {
                try {
                    Long studyId = Long.valueOf(studyIdHeader);
                    if (!studyService.isUserStudyMember(studyId, memberId)) {
                        throw new AccessDeniedException("해당 스터디의 멤버가 아님");
                    }
                } catch (NumberFormatException e) {
                    throw new AccessDeniedException("잘못된 스터디 ID 형식");
                }
            }
        }



        return message;
    }

    private Long extractStudyId(String destination) {
        if (destination == null) return null;

        // 예: /publish/chat.send/7
        String[] parts = destination.split("/");
        try {
            return Long.valueOf(parts[parts.length - 1]); // 마지막이 studyId라고 가정
        } catch (NumberFormatException e) {
            log.error("Invalid destination format: {}", destination);
            return null;
        }
    }
}
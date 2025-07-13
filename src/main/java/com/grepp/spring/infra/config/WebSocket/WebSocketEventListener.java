package com.grepp.spring.infra.config.WebSocket;


import com.grepp.spring.app.controller.api.chat.ParticipantResponse;
import com.grepp.spring.app.controller.api.chat.SessionUserInfo;
import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.chat.service.ChatService;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor

public class WebSocketEventListener {

    private final WebSocketSessionTracker tracker;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @EventListener
    public void handleConnect(SessionConnectEvent event ) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        if (accessor.getUser() != null) {

            Authentication authentication = (Authentication) accessor.getUser();
            Principal principal = (Principal) authentication.getPrincipal();

            long memberId = principal.getMemberId();
            String email = principal.getUsername();
            String nickname = memberRepository.findNicknameById(memberId);

            String studyIdHeader = accessor.getFirstNativeHeader("studyId");
            String sessionId = accessor.getSessionId();

            if (studyIdHeader != null && sessionId != null) {
                try {
                    Long studyId = Long.valueOf(studyIdHeader);
                    tracker.addSession(sessionId, studyId, email, nickname);
                    broadcastParticipants(studyId);
                } catch (NumberFormatException e) {
                    // 로그 등 처리
                }
            }
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        if (sessionId != null) {
            SessionUserInfo info = tracker.getSession(sessionId);
            if (info != null) {
                tracker.removeUser(info.studyId(), info.email());
                tracker.removeSession(sessionId);
                broadcastParticipants(info.studyId());
            }
        }
    }

    private void broadcastParticipants(Long studyId) {

        List<ParticipantResponse> current = chatService.getOnlineParticipants(studyId);

        messagingTemplate.convertAndSend("/subscribe/" + studyId + "/participants", current);
        System.out.println("broadcasting to /subscribe/" + studyId + "/participants : " + current.size() + "명");
    }



}

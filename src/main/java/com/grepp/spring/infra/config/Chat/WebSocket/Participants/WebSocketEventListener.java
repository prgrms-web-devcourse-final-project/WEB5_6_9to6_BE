package com.grepp.spring.infra.config.Chat.WebSocket.Participants;


import com.grepp.spring.app.controller.api.chat.ParticipantResponse;
import com.grepp.spring.app.controller.api.chat.SessionUserInfo;
import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.chat.service.ChatService;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.infra.config.Chat.WebSocket.WebSocketSessionTracker;
import com.grepp.spring.infra.config.Chat.WebSocket.WorkerManager;
import com.grepp.spring.infra.response.CommonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String,String> redisTemplate;
    private final WorkerManager workerManager;

    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        if (accessor.getUser() != null) {
            Authentication authentication = (Authentication) accessor.getUser();
            Principal principal = (Principal) authentication.getPrincipal();

            long memberId = principal.getMemberId();
            String email = principal.getUsername();
            String nickname = memberRepository.getNickname(memberId);
            String image = memberRepository.getAvatarImage(memberId);

            String studyIdHeader = accessor.getFirstNativeHeader("studyId");
            String sessionId = accessor.getSessionId();

            if (studyIdHeader != null && sessionId != null) {
                try {
                    Long studyId = Long.valueOf(studyIdHeader);

                    // 메모리 및 Redis 등록
                    tracker.addSession(sessionId, memberId,studyId, email, nickname,image);
                    redisTemplate.<String,String>opsForHash().put("nicknames:" + studyId, email, memberId + ":" + nickname);
                    redisTemplate.<String,String>opsForHash().put("participants:" + studyId, sessionId, email);

                    //접속자 broadcast
                    broadcastParticipants(studyId);
                    // 워커 등록
                    workerManager.startWorker(studyId);
                } catch (NumberFormatException e) {
                    System.out.println(" studyId 파싱 실패: " + studyIdHeader);
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
                Long studyId = info.studyId();
                String email = info.email();

                // 메모리 제거
                tracker.removeSession(sessionId);
                tracker.removeUser(studyId, email);

                // Redis 제거
                redisTemplate.<String,String>opsForHash().delete("participants:" + studyId, sessionId);


                broadcastParticipants(studyId);

                // 워커 제거
                workerManager.stopWorker(studyId);
            }
        }
    }

    private void broadcastParticipants(Long studyId) {

        List<ParticipantResponse> current = chatService.getOnlineParticipants(studyId);
        CommonResponse<List<ParticipantResponse>> response = CommonResponse.success(current);

        messagingTemplate.convertAndSend("/subscribe/" + studyId + "/participants", response);
        System.out.println("broadcasting to /subscribe/" + studyId + "/participants : " + current.size() + "명");
    }



}

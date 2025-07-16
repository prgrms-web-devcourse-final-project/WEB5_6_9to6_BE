package com.grepp.spring.app.model.chat.service;

import com.grepp.spring.app.controller.api.chat.ChatHistoryResponse;
import com.grepp.spring.app.controller.api.chat.ParticipantResponse;
import com.grepp.spring.app.controller.websocket.payload.ChatMessageRequest;
import com.grepp.spring.app.controller.websocket.payload.ChatMessageResponse;
import com.grepp.spring.app.model.chat.entity.Chat;
import com.grepp.spring.app.model.chat.entity.ChatRoom;
import com.grepp.spring.app.model.chat.repository.ChatRepository;
import com.grepp.spring.app.model.chat.repository.ChatRoomRepository;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.infra.config.Chat.WebSocket.WebSocketSessionTracker;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.infra.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {


    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final WebSocketSessionTracker tracker;
    private final RedisTemplate redisTemplate;

    @Transactional
    public ChatMessageResponse saveChatMessage(Long studyId, ChatMessageRequest request,
        Long senderId) {
        ChatRoom chatRoom = chatRoomRepository.findByStudy_StudyId(studyId)
            .orElseThrow(() -> new NotFoundException("Invalid studyId"));

        String nickname = memberRepository.findNicknameById(senderId);

        Chat chat = request.toEntity(chatRoom, senderId, nickname);

        chatRepository.save(chat);

        return ChatMessageResponse.from(chat);


    }


    @Transactional
    public List<ChatHistoryResponse> findChat(Long studyId, Long memberId, String username) {

        List<Chat> chats = chatRepository.findAllRelevantChats(studyId, username, memberId);

        return chats.stream()
            .sorted(Comparator.comparing(Chat::getCreatedAt).reversed()) // 가장 최근이 먼저
            .map(ChatHistoryResponse::from)
            .collect(Collectors.toList());
    }

    // 채팅방 생성
    public ChatRoom createChatRoom(Study study) {
        ChatRoom chatRoom = ChatRoom.builder()
            .study(study)
            .build();
        return chatRoomRepository.save(chatRoom);
    }

    public List<ParticipantResponse> getOnlineParticipants(Long studyId) {
        Map<Object, Object> sessions = redisTemplate.opsForHash().entries("participants:" + studyId);
        Map<Object, Object> nicknames = redisTemplate.opsForHash().entries("nicknames:" + studyId);

        Set<String> uniqueEmails = sessions.values().stream()
            .map(Object::toString)
            .collect(Collectors.toSet());

        return uniqueEmails.stream()
            .map(email -> {
                String value = (String) nicknames.get(email); // "123:Alice"
                if (value == null || !value.contains(":")) return null;

                String[] parts = value.split(":", 2);
                Long memberId = Long.valueOf(parts[0]);
                String nickname = parts[1];

                return new ParticipantResponse(memberId, nickname, "ONLINE");
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    // 매일 새벽 4시에 실행 (필요에 따라 수정 가능)
    @Scheduled(cron = "0 0 4 * * ?")
    @Transactional
    public void deleteOldMessages() {
        LocalDateTime threshold = LocalDateTime.now().minusWeeks(2);
        int deletedCount = chatRepository.deleteByCreatedAtBefore(threshold);
        log.info(" 삭제된 메시지 수: " + deletedCount);
    }
}








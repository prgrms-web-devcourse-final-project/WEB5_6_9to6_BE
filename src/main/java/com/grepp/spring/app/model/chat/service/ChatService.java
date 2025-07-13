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
import com.grepp.spring.infra.config.WebSocket.WebSocketSessionTracker;
import com.grepp.spring.infra.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {


    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final WebSocketSessionTracker tracker;

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

        // 4. 병합 후 정렬
        return chats.stream()
            .sorted(Comparator.comparing(Chat::getCreatedAt).reversed()) // 가장 최근이 먼저
            .map(ChatHistoryResponse::from)
            .collect(Collectors.toList());
    }

    public List<ParticipantResponse> getOnlineParticipants(Long studyId) {
        Map<String, String> onlineUsers = tracker.getConnectedUsers(studyId);

        return onlineUsers.entrySet().stream()
            .map(entry -> {
                String email = entry.getKey();
                String nickname = entry.getValue();
                Long memberId = memberRepository.findIdByEmail(email);

                return new ParticipantResponse(memberId, nickname, "ONLINE");
            })
            .collect(Collectors.toList());
    }
}






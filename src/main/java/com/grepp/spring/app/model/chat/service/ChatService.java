package com.grepp.spring.app.model.chat.service;

import com.grepp.spring.app.model.chat.dto.response.ChatHistoryResponse;
import com.grepp.spring.app.model.chat.dto.response.ChatPageResponse;
import com.grepp.spring.app.model.chat.dto.response.ParticipantResponse;
import com.grepp.spring.app.model.chat.dto.response.SessionUserInfo;
import com.grepp.spring.app.controller.websocket.payload.ChatMessageRequest;
import com.grepp.spring.app.controller.websocket.payload.ChatMessageResponse;
import com.grepp.spring.app.model.chat.entity.Chat;
import com.grepp.spring.app.model.chat.entity.ChatRoom;
import com.grepp.spring.app.model.chat.repository.ChatRepository;
import com.grepp.spring.app.model.chat.repository.ChatRoomRepository;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.repository.StudyRepository;
import com.grepp.spring.infra.config.Chat.WebSocket.WebSocketSessionTracker;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {


    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final WebSocketSessionTracker tracker;
    private final RedisTemplate redisTemplate;
    private final StudyRepository studyRepository;

    @Transactional
    public ChatMessageResponse saveChatMessage(Long studyId, ChatMessageRequest request,
        Long senderId) {
        ChatRoom chatRoom = chatRoomRepository.findByStudy_StudyId(studyId)
            .orElseThrow(() -> new NotFoundException("Invalid studyId"));

        String nickname = memberRepository.getNickname(senderId);



        Chat chat = request.toEntity(chatRoom, senderId, nickname);
        String image = memberRepository.getAvatarImage(senderId);
        System.out.println("senderId: " + senderId + ", image: " + image);

        chatRepository.save(chat);


        return ChatMessageResponse.from(chat, image);


    }


    @Transactional(readOnly = true)
    public ChatPageResponse findChat(Long studyId, Long memberId, String username,LocalDateTime cursorCreatedAt, Long lastId,int pageSize) {

        List<Chat> chats = chatRepository.findAllRelevantChatsPage(
            studyId,
            username,
            memberId,
            cursorCreatedAt,
            lastId,
            pageSize+1);

        boolean hasNext = chats.size() > pageSize;

        if (hasNext) {
            chats = chats.subList(0, pageSize); // 딱 pageSize만 남기기
        }

        // 3. 정렬 뒤집기 (DESC → ASC)
        Collections.reverse(chats);

        // 1. senderId만 수집
        Set<Long> senderIds = chats.stream()
            .map(Chat::getSenderId)
            .collect(Collectors.toSet());

        // 2. sender 정보 조회
        List<Member> members = memberRepository.findAllById(senderIds);

        // 3. id → image 매핑
        Map<Long, String> idToImage = members.stream()
            .collect(Collectors.toMap(Member::getId, Member::getAvatarImage));

        List<ChatHistoryResponse> responses = chats.stream()
            .map(chat -> {
                String image = idToImage.get(chat.getSenderId());
                return ChatHistoryResponse.from(chat, image);
            })
            .toList();

        // 마지막 채팅 정보 (커서 갱신용)
        Chat lastChat = chats.isEmpty() ? null : chats.get(chats.size() - 1);

        return new ChatPageResponse(
            responses,
            lastChat != null ? lastChat.getCreatedAt() : null,
            lastChat != null ? lastChat.getId() : null,
            hasNext
        );



    }

    // 채팅방 생성
    public void createChatRoom(Long studyId) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new NotFoundException("스터디를 찾을 수 없습니다."));

        ChatRoom chatRoom = ChatRoom.builder()
            .study(study)
            .build();

        chatRoomRepository.save(chatRoom);
    }

    public List<ParticipantResponse> getOnlineParticipants(Long studyId) {
        Map<String, SessionUserInfo> connectedUsers = tracker.getConnectedUsers(studyId);



        return connectedUsers.values().stream()
            .map(userInfo -> new ParticipantResponse(
                userInfo.memberId(),   // memberId 추가해야 한다면 SessionUserInfo에 저장 필요
                userInfo.nickname(),
                userInfo.image(),
                "ONLINE"
                      // 이미지 추가
            ))
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








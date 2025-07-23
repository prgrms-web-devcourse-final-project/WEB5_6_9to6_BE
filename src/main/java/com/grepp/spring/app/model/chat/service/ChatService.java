package com.grepp.spring.app.model.chat.service;

import com.grepp.spring.app.controller.api.chat.ChatHistoryResponse;
import com.grepp.spring.app.controller.api.chat.ParticipantResponse;
import com.grepp.spring.app.controller.api.chat.SessionUserInfo;
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
import com.grepp.spring.infra.util.SecurityUtil;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
    private final StudyRepository studyRepository;

    @Transactional
    public ChatMessageResponse saveChatMessage(Long studyId, ChatMessageRequest request,
        Long senderId) {
        ChatRoom chatRoom = chatRoomRepository.findByStudy_StudyId(studyId)
            .orElseThrow(() -> new NotFoundException("Invalid studyId"));

        String nickname = memberRepository.findNicknameById(senderId);

        Chat chat = request.toEntity(chatRoom, senderId, nickname);
        String image = memberRepository.findAvatarImageById(senderId);
        System.out.println("senderId: " + senderId + ", image: " + image);

        chatRepository.save(chat);


        return ChatMessageResponse.from(chat, image);


    }


    @Transactional
    public List<ChatHistoryResponse> findChat(Long studyId, Long memberId, String username) {

        List<Chat> chats = chatRepository.findAllRelevantChats(studyId, username, memberId);

        // 1. senderId만 수집
        Set<Long> senderIds = chats.stream()
            .map(Chat::getSenderId)
            .collect(Collectors.toSet());

        // 2. sender 정보 조회
        List<Member> members = memberRepository.findAllById(senderIds);

        // 3. id → image 매핑
        Map<Long, String> idToImage = members.stream()
            .collect(Collectors.toMap(Member::getId, Member::getAvatarImage));



        return chats.stream()
            .sorted(Comparator.comparing(Chat::getCreatedAt).reversed())
            .map(chat -> {
                String image = idToImage.get(chat.getSenderId());
                return ChatHistoryResponse.from(chat, image);
            })
            .collect(Collectors.toList());
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








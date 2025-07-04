package com.grepp.studium.chat_room.service;

import com.grepp.studium.chat.domain.Chat;
import com.grepp.studium.chat.repos.ChatRepository;
import com.grepp.studium.chat_room.domain.ChatRoom;
import com.grepp.studium.chat_room.model.ChatRoomDTO;
import com.grepp.studium.chat_room.repos.ChatRoomRepository;
import com.grepp.studium.util.NotFoundException;
import com.grepp.studium.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    public ChatRoomService(final ChatRoomRepository chatRoomRepository,
            final ChatRepository chatRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRepository = chatRepository;
    }

    public List<ChatRoomDTO> findAll() {
        final List<ChatRoom> chatRooms = chatRoomRepository.findAll(Sort.by("roomId"));
        return chatRooms.stream()
                .map(chatRoom -> mapToDTO(chatRoom, new ChatRoomDTO()))
                .toList();
    }

    public ChatRoomDTO get(final Integer roomId) {
        return chatRoomRepository.findById(roomId)
                .map(chatRoom -> mapToDTO(chatRoom, new ChatRoomDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ChatRoomDTO chatRoomDTO) {
        final ChatRoom chatRoom = new ChatRoom();
        mapToEntity(chatRoomDTO, chatRoom);
        return chatRoomRepository.save(chatRoom).getRoomId();
    }

    public void update(final Integer roomId, final ChatRoomDTO chatRoomDTO) {
        final ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(chatRoomDTO, chatRoom);
        chatRoomRepository.save(chatRoom);
    }

    public void delete(final Integer roomId) {
        chatRoomRepository.deleteById(roomId);
    }

    private ChatRoomDTO mapToDTO(final ChatRoom chatRoom, final ChatRoomDTO chatRoomDTO) {
        chatRoomDTO.setRoomId(chatRoom.getRoomId());
        chatRoomDTO.setCreatedAt(chatRoom.getCreatedAt());
        chatRoomDTO.setActivated(chatRoom.getActivated());
        return chatRoomDTO;
    }

    private ChatRoom mapToEntity(final ChatRoomDTO chatRoomDTO, final ChatRoom chatRoom) {
        chatRoom.setCreatedAt(chatRoomDTO.getCreatedAt());
        chatRoom.setActivated(chatRoomDTO.getActivated());
        return chatRoom;
    }

    public ReferencedWarning getReferencedWarning(final Integer roomId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(NotFoundException::new);
        final Chat roomChat = chatRepository.findFirstByRoom(chatRoom);
        if (roomChat != null) {
            referencedWarning.setKey("chatRoom.chat.room.referenced");
            referencedWarning.addParam(roomChat.getChatId());
            return referencedWarning;
        }
        return null;
    }

}

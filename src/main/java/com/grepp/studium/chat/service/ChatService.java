package com.grepp.studium.chat.service;

import com.grepp.studium.chat.domain.Chat;
import com.grepp.studium.chat.model.ChatDTO;
import com.grepp.studium.chat.repos.ChatRepository;
import com.grepp.studium.chat_room.domain.ChatRoom;
import com.grepp.studium.chat_room.repos.ChatRoomRepository;
import com.grepp.studium.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatService(final ChatRepository chatRepository,
            final ChatRoomRepository chatRoomRepository) {
        this.chatRepository = chatRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public List<ChatDTO> findAll() {
        final List<Chat> chats = chatRepository.findAll(Sort.by("chatId"));
        return chats.stream()
                .map(chat -> mapToDTO(chat, new ChatDTO()))
                .toList();
    }

    public ChatDTO get(final Integer chatId) {
        return chatRepository.findById(chatId)
                .map(chat -> mapToDTO(chat, new ChatDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ChatDTO chatDTO) {
        final Chat chat = new Chat();
        mapToEntity(chatDTO, chat);
        return chatRepository.save(chat).getChatId();
    }

    public void update(final Integer chatId, final ChatDTO chatDTO) {
        final Chat chat = chatRepository.findById(chatId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(chatDTO, chat);
        chatRepository.save(chat);
    }

    public void delete(final Integer chatId) {
        chatRepository.deleteById(chatId);
    }

    private ChatDTO mapToDTO(final Chat chat, final ChatDTO chatDTO) {
        chatDTO.setChatId(chat.getChatId());
        chatDTO.setReceiverId(chat.getReceiverId());
        chatDTO.setSenderId(chat.getSenderId());
        chatDTO.setContent(chat.getContent());
        chatDTO.setSentAt(chat.getSentAt());
        chatDTO.setActivated(chat.getActivated());
        chatDTO.setRoom(chat.getRoom() == null ? null : chat.getRoom().getRoomId());
        return chatDTO;
    }

    private Chat mapToEntity(final ChatDTO chatDTO, final Chat chat) {
        chat.setReceiverId(chatDTO.getReceiverId());
        chat.setSenderId(chatDTO.getSenderId());
        chat.setContent(chatDTO.getContent());
        chat.setSentAt(chatDTO.getSentAt());
        chat.setActivated(chatDTO.getActivated());
        final ChatRoom room = chatDTO.getRoom() == null ? null : chatRoomRepository.findById(chatDTO.getRoom())
                .orElseThrow(() -> new NotFoundException("room not found"));
        chat.setRoom(room);
        return chat;
    }

}

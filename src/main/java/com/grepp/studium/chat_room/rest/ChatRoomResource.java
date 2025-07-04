package com.grepp.studium.chat_room.rest;

import com.grepp.studium.chat_room.model.ChatRoomDTO;
import com.grepp.studium.chat_room.service.ChatRoomService;
import com.grepp.studium.util.ReferencedException;
import com.grepp.studium.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/chatRooms", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatRoomResource {

    private final ChatRoomService chatRoomService;

    public ChatRoomResource(final ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomDTO>> getAllChatRooms() {
        return ResponseEntity.ok(chatRoomService.findAll());
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomDTO> getChatRoom(
            @PathVariable(name = "roomId") final Integer roomId) {
        return ResponseEntity.ok(chatRoomService.get(roomId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createChatRoom(
            @RequestBody @Valid final ChatRoomDTO chatRoomDTO) {
        final Integer createdRoomId = chatRoomService.create(chatRoomDTO);
        return new ResponseEntity<>(createdRoomId, HttpStatus.CREATED);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<Integer> updateChatRoom(
            @PathVariable(name = "roomId") final Integer roomId,
            @RequestBody @Valid final ChatRoomDTO chatRoomDTO) {
        chatRoomService.update(roomId, chatRoomDTO);
        return ResponseEntity.ok(roomId);
    }

    @DeleteMapping("/{roomId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteChatRoom(
            @PathVariable(name = "roomId") final Integer roomId) {
        final ReferencedWarning referencedWarning = chatRoomService.getReferencedWarning(roomId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        chatRoomService.delete(roomId);
        return ResponseEntity.noContent().build();
    }

}

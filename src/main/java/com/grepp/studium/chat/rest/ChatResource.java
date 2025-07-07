package com.grepp.studium.chat.rest;

import com.grepp.studium.chat.model.ChatDTO;
import com.grepp.studium.chat.service.ChatService;
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
@RequestMapping(value = "/api/chats", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatResource {

    private final ChatService chatService;

    public ChatResource(final ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<List<ChatDTO>> getAllChats() {
        return ResponseEntity.ok(chatService.findAll());
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDTO> getChat(@PathVariable(name = "chatId") final Integer chatId) {
        return ResponseEntity.ok(chatService.get(chatId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createChat(@RequestBody @Valid final ChatDTO chatDTO) {
        final Integer createdChatId = chatService.create(chatDTO);
        return new ResponseEntity<>(createdChatId, HttpStatus.CREATED);
    }

    @PutMapping("/{chatId}")
    public ResponseEntity<Integer> updateChat(@PathVariable(name = "chatId") final Integer chatId,
            @RequestBody @Valid final ChatDTO chatDTO) {
        chatService.update(chatId, chatDTO);
        return ResponseEntity.ok(chatId);
    }

    @DeleteMapping("/{chatId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteChat(@PathVariable(name = "chatId") final Integer chatId) {
        chatService.delete(chatId);
        return ResponseEntity.noContent().build();
    }

}

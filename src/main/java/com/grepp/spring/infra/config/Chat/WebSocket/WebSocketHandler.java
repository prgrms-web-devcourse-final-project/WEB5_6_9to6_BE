package com.grepp.spring.infra.config.Chat.WebSocket;

import com.grepp.spring.app.model.chat.dto.response.SessionUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final WorkerManager workerManager;
    private final WebSocketSessionTracker sessionTracker;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 세션에 저장된 사용자 정보 얻기
        SessionUserInfo userInfo = sessionTracker.getSession(session.getId());
        if (userInfo != null) {
            Long studyId = userInfo.studyId();
            workerManager.startWorker(studyId);
        }
    }



}

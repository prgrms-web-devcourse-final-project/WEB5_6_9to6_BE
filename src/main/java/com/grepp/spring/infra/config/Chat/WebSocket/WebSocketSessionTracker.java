package com.grepp.spring.infra.config.Chat.WebSocket;

import com.grepp.spring.app.model.chat.dto.response.SessionUserInfo;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class WebSocketSessionTracker {



    // studyId → (email → nickname)
    private final Map<Long, Map<String, SessionUserInfo>> connectedUsersByStudy = new ConcurrentHashMap<>();

    private final Map<String, SessionUserInfo> sessionMap = new ConcurrentHashMap<>();

    public void addSession(String sessionId,Long memberId, Long studyId, String email, String nickname, String image) {

        SessionUserInfo userInfo = new SessionUserInfo(memberId, studyId, email, nickname, image);
        sessionMap.put(sessionId, userInfo);

        connectedUsersByStudy
            .computeIfAbsent(studyId, k -> new ConcurrentHashMap<>())
            .put(email, userInfo);



        System.out.println("addUser: studyId=" + studyId + ", email=" + email + ", nickname=" + nickname);

    }

    public void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

    public SessionUserInfo getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public Map<String, SessionUserInfo> getConnectedUsers(Long studyId) {
        return connectedUsersByStudy.getOrDefault(studyId, Collections.emptyMap());
    }



    public void removeUser(Long studyId, String email) {
        Map<String, SessionUserInfo> users = connectedUsersByStudy.get(studyId);
        if (users != null) {
            users.remove(email);
            if (users.isEmpty()) {
                connectedUsersByStudy.remove(studyId);
            }
        }
    }
}


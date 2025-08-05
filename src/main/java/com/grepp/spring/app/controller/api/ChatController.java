package com.grepp.spring.app.controller.api;

import com.grepp.spring.app.model.chat.dto.request.ChatCursorRequest;
import com.grepp.spring.app.model.chat.dto.response.ChatPageResponse;
import com.grepp.spring.app.model.chat.dto.response.ParticipantResponse;
import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.chat.service.ChatService;
import com.grepp.spring.app.model.study.service.StudyService;
import com.grepp.spring.infra.config.Chat.WebSocket.WebSocketSessionTracker;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.ResponseCode;
import com.grepp.spring.infra.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "채팅 API", description = "채팅관련API")
@RestController
@RequestMapping(value = "/api/v1/chats", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final WebSocketSessionTracker webSocketSessionTracker;
    private final StudyService studyService;




    // 공통 에러 응답 메서드
    private ResponseEntity<Map<String, Object>> errorResponse(String code, String message, HttpStatus status) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("code", code);
        error.put("message", message);
        return ResponseEntity.status(status).body(error);
    }


    // 최초 접속시 사용
    // 현재 접속 중인 사용자 목록 조회
    @GetMapping("/{studyId}/participants")
    @ApiResponse(responseCode = "200")
    @Operation(summary = "※주의※ 이런 메서드는 없습니다.",
        description = "이 메서드를 발견했다면 당장 뒤로가기를 눌러 도망 가십시오.\n"
            + "웹소켓으로 변경해서 안쓸거 같습니다.")
    public ResponseEntity<CommonResponse<List<ParticipantResponse>>> getOnlineParticipants(@PathVariable Long studyId) {

        List<ParticipantResponse> participants = chatService.getOnlineParticipants(studyId);


        return ResponseEntity.ok(CommonResponse.success(participants));
    }

    // 채팅 내역 조회

    @GetMapping("/{studyId}/history")
    @ApiResponse(responseCode = "200")
    @Operation(summary = "최근 14일간의 채팅 내역 확인",
        description = "스터디 내의 전체 채팅, 보낸 귓속말, 받은 귓속말이 표시됩니다.<br><br>"
            + "가장 처음 메세지를 보려면 리퀘스트 오브젝트에<br>"
            + "{<br>"
            + "  \"cursorCreatedAt\": null,<br>"
            + "  \"lastChatId\": null<br>"
            + "}<br><br>"
            +"를 넣어주세요<br>"
            + "응답에 cursorCreatedAt, lastChatId가 포함되어 있습니다. 요청으로 다시 requestObject에 넣어 보내주면 다음 메세지가 갑니다.")
    public ResponseEntity<CommonResponse<ChatPageResponse>> getChatHistory(
        @PathVariable Long studyId,
        @ModelAttribute ChatCursorRequest request,
        @RequestParam(defaultValue = "30") int pageSize,
        Authentication authentication) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        Principal principal = (Principal) authentication.getPrincipal();

        boolean isMember = studyService.isUserStudyMember(memberId, studyId);

        if(!isMember){
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse.error(ResponseCode.UNAUTHORIZED));
        }


        ChatPageResponse responses = chatService.findChat(
            studyId,
            memberId,
            principal.getUsername(),
            request.cursorCreatedAt(),
            request.lastChatId(),
            pageSize);

        return ResponseEntity.ok(CommonResponse.success(responses));
    }

    @Operation(
        summary = "채팅 메세지 전송(공개/귓속말)",
        description = """ 
        전체 채팅과 귓속말은 요청 형태는 같지만 구독경로는 따로 잡아 주셔야 합니다.
        \n
        제가 확인하려고 따로 만들어 놓은 html을 확인하시면 좋습니다.
        http://3.37.19.66/chat.html?studyId=1
        \n
        근데 스터디에 가입 안되있으면 채팅 못칩니다. 수고하세요.
        \n
        일단, 
        user1@example.com
        user2@example.com 
        얘들은 1번 스터디에 가입한 거 확인했습니다.
        

        - **Protocol:** WebSocket
        - **Message Format:**
        - **subscribe path:**
        
        전체 채팅
        /subscribe/\\${studyId}
        
        귓속말
        /user/queue/messages
        
        
        request json 
<pre><code>{
  "receiverEmail": "bob@naver.com",
  "receiverNickname": "투명드래건빵",
  "content": "안녕하세요!"
}
</code></pre>

<pre><code>response json</code></pre>

<pre><code>{
  "code": "0000",
  "message": "채팅 메시지 전송 성공",
  "data": {
    "senderId": 123,
    "senderNickname": "보내는사람",
    "receiverId": 456,
    "receiverNickname": "받는사람",
    "content": "안녕하세요!"
  }
}
</code></pre>

        
        """
    )
    @GetMapping("/ws-docs")
    public ResponseEntity<String> chatDocs() {
        return ResponseEntity.ok("Swagger 문서용 설명입니다.");
    }

    @Operation(
        summary = "채팅 참가자 확인",
        description = """ 
        구독만 하면 응답이 갑니다.
        현재 처음 접속한 사람은 잘 확인이 안되는 버그가 있습니다.
        \n
        F5 난타 하시면 꽤 오래 걸려서 됩니다.
        잘 안되면 다른 사람 들어오면 확실히 됩니다.\n
        스터디 입장시에 스레드가 돌게 해놨는데 구독이 스레드가 돌기전에 돌아서 잘 안되는 것 같습니다.
        해결은 쉽지 않을 것 같습니다.
        
        

        - **Protocol:** WebSocket
        - **Message Format:**
        - **subscribe path:**
        
        참가자 수 확인
        /subscribe/\\${studyId}/participants
        
        
        
        
        
        
             Request json
        
                 없음

        
            -**Response json**-
<pre><code>{
  "code": "0000",
  "message": "접속자 목록 조회 성공",
  "data": [
    {
      "memberId": 3,
      "nickname": "철수",
      "status": "ONLINE"
    },
    {
      "memberId": 5,
      "nickname": "영희",
      "status": "ONLINE"
    }
  ]
    }</code><pre>
        """
    )
    @GetMapping("/ws-docs1")
    public ResponseEntity<String> participantsDocs() {
        return ResponseEntity.ok("Swagger 문서용 설명입니다.");
    }

}

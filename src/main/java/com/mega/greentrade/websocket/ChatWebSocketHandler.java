package com.mega.greentrade.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mega.greentrade.repository.UserRepository;
import com.mega.greentrade.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private final UserRepository userRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private static final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String chatroomId = getChatroomId(session);
        rooms.computeIfAbsent(chatroomId, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String chatroomId = getChatroomId(session);
        JsonNode json = objectMapper.readTree(message.getPayload());
        int senderUserno = json.get("userno").asInt();
        String text = json.get("message").asText();

        // DB 저장
        chatService.saveMessage(Integer.parseInt(chatroomId), senderUserno, text);

        // 닉네임 조회
        String nickname = userRepository.findById(senderUserno)
                .map(u -> u.getNickname())
                .orElse("알 수 없음");

        // 브로드캐스트 메시지
        Map<String, Object> broadcastData = new HashMap<>();
        broadcastData.put("userno", senderUserno);
        broadcastData.put("nickname", nickname);
        broadcastData.put("message", text);
        broadcastData.put("sentAt", LocalDateTime.now().format(TIME_FMT));

        TextMessage broadcastMsg = new TextMessage(objectMapper.writeValueAsString(broadcastData));

        Set<WebSocketSession> participants = rooms.get(chatroomId);
        if (participants != null) {
            for (WebSocketSession s : participants) {
                if (s.isOpen()) {
                    s.sendMessage(broadcastMsg);
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String chatroomId = getChatroomId(session);
        Set<WebSocketSession> participants = rooms.get(chatroomId);
        if (participants != null) {
            participants.remove(session);
        }
    }

    private String getChatroomId(WebSocketSession session) {
        String path = session.getUri().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}

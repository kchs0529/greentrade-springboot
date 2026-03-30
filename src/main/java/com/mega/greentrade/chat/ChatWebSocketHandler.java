package com.mega.greentrade.chat;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    // chatroomId -> 세션 목록
    private static final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String chatroomId = getChatroomId(session);
        rooms.computeIfAbsent(chatroomId, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String chatroomId = getChatroomId(session);
        Set<WebSocketSession> participants = rooms.get(chatroomId);
        if (participants != null) {
            for (WebSocketSession s : participants) {
                if (s.isOpen()) {
                    s.sendMessage(message);
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

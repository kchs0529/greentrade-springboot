package com.mega.greentrade.service;

import com.mega.greentrade.dto.ChatMessageDTO;
import com.mega.greentrade.dto.ChatRoomListDTO;
import com.mega.greentrade.entity.ChatMessage;
import com.mega.greentrade.entity.ChatRoom;
import com.mega.greentrade.repository.ChatMessageRepository;
import com.mega.greentrade.repository.ChatRoomRepository;
import com.mega.greentrade.repository.ProductRepository;
import com.mega.greentrade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public ChatMessage saveMessage(int chatroomId, int senderUserno, String message) {
        ChatMessage msg = new ChatMessage();
        msg.setChatroomId(chatroomId);
        msg.setSenderUserno(senderUserno);
        msg.setMessage(message);
        msg.setSentAt(LocalDateTime.now());
        return chatMessageRepository.save(msg);
    }

    public List<ChatMessageDTO> getMessages(int chatroomId) {
        return chatMessageRepository.findByChatroomIdOrderBySentAtAsc(chatroomId)
                .stream()
                .map(msg -> {
                    ChatMessageDTO dto = new ChatMessageDTO();
                    dto.setSenderUserno(msg.getSenderUserno());
                    dto.setMessage(msg.getMessage());
                    dto.setSentAt(msg.getSentAt().format(TIME_FMT));
                    userRepository.findById(msg.getSenderUserno())
                            .ifPresent(u -> dto.setSenderNickname(u.getNickname()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<ChatRoomListDTO> getChatroomListForUser(int userno) {
        List<ChatRoom> rooms = chatRoomRepository.findByBuyerOrSeller(userno, userno);
        return rooms.stream().map(room -> {
            ChatRoomListDTO dto = new ChatRoomListDTO();
            dto.setChatroomId(room.getChatroomId());
            dto.setRole(room.getBuyer() == userno ? "구매자" : "판매자");

            productRepository.findById(room.getSellproduct()).ifPresent(p -> {
                dto.setProductTitle(p.getTitle());
                dto.setProductImage(p.getImage());
            });

            int otherUserno = (room.getBuyer() == userno) ? room.getSeller() : room.getBuyer();
            userRepository.findById(otherUserno)
                    .ifPresent(u -> dto.setOtherNickname(u.getNickname()));

            chatMessageRepository.findTopByChatroomIdOrderBySentAtDesc(room.getChatroomId())
                    .ifPresent(msg -> {
                        dto.setLastMessage(msg.getMessage());
                        dto.setLastMessageAt(msg.getSentAt());
                    });

            return dto;
        }).collect(Collectors.toList());
    }
}

package com.mega.greentrade.controller;
import com.mega.greentrade.entity.ChatRoom;
import com.mega.greentrade.repository.ChatRoomRepository;

import com.mega.greentrade.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/chat")
    @Transactional
    public String chat(@RequestParam int productno,
                       @AuthenticationPrincipal CustomUserDetails userDetails,
                       Model model) {
        int buyer = userDetails.getUserno();
        int chatroomId = chatRoomRepository.findByBuyerAndSellproduct(buyer, productno)
                .map(ChatRoom::getChatroomId)
                .orElseGet(() -> {
                    ChatRoom room = new ChatRoom();
                    room.setBuyer(buyer);
                    room.setSellproduct(productno);
                    return chatRoomRepository.save(room).getChatroomId();
                });
        model.addAttribute("chatroomId", chatroomId);
        model.addAttribute("userno", buyer);
        model.addAttribute("productno", productno);
        return "chat/chatting";
    }
}

package com.mega.greentrade.chat;

import com.mega.greentrade.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatDAO chatDAO;

    @GetMapping("/chat")
    public String chat(@RequestParam int productno,
                       @AuthenticationPrincipal CustomUserDetails userDetails,
                       Model model) {
        int chatroomId = chatDAO.getOrCreateChatroom(userDetails.getUserno(), productno);
        model.addAttribute("chatroomId", chatroomId);
        model.addAttribute("userno", userDetails.getUserno());
        model.addAttribute("productno", productno);
        return "chat/chatting";
    }
}

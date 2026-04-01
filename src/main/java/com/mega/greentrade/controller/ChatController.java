package com.mega.greentrade.controller;

import com.mega.greentrade.dto.ChatMessageDTO;
import com.mega.greentrade.dto.ChatRoomListDTO;
import com.mega.greentrade.entity.ChatRoom;
import com.mega.greentrade.entity.Product;
import com.mega.greentrade.entity.User;
import com.mega.greentrade.repository.ChatRoomRepository;
import com.mega.greentrade.repository.ProductRepository;
import com.mega.greentrade.repository.UserRepository;
import com.mega.greentrade.security.CustomUserDetails;
import com.mega.greentrade.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat", description = "채팅 관련 API")
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;

    @Operation(summary = "채팅 시작 (구매자 - 상품 페이지에서 진입)")
    @GetMapping("/chat")
    @Transactional
    public String chat(@RequestParam int productno,
                       @AuthenticationPrincipal CustomUserDetails userDetails,
                       Model model) {
        int buyer = userDetails.getUserno();
        Product product = productRepository.findById(productno).orElse(null);
        if (product == null) return "redirect:/product/list";

        // 판매자가 자기 상품에 채팅 방지
        if (product.getUserno() == buyer) return "redirect:/product/detail/" + productno;

        int seller = product.getUserno();

        int chatroomId = chatRoomRepository.findByBuyerAndSellproduct(buyer, productno)
                .map(existingRoom -> {
                    // 기존 채팅방에 seller 정보가 없으면 업데이트
                    if (existingRoom.getSeller() == 0) {
                        existingRoom.setSeller(seller);
                        chatRoomRepository.save(existingRoom);
                    }
                    return existingRoom.getChatroomId();
                })
                .orElseGet(() -> {
                    ChatRoom room = new ChatRoom();
                    room.setBuyer(buyer);
                    room.setSeller(seller);
                    room.setSellproduct(productno);
                    return chatRoomRepository.save(room).getChatroomId();
                });

        return "redirect:/chat/room/" + chatroomId;
    }

    @Operation(summary = "채팅방 입장 (구매자/판매자 공통)")
    @GetMapping("/chat/room/{chatroomId}")
    public String chatRoom(@PathVariable int chatroomId,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model) {
        int userno = userDetails.getUserno();
        ChatRoom room = chatRoomRepository.findById(chatroomId).orElse(null);
        if (room == null) return "redirect:/chat/list";

        // 접근 권한 확인 (구매자 또는 판매자만)
        if (room.getBuyer() != userno && room.getSeller() != userno) {
            return "redirect:/chat/list";
        }

        List<ChatMessageDTO> messages = chatService.getMessages(chatroomId);

        int otherUserno = (room.getBuyer() == userno) ? room.getSeller() : room.getBuyer();
        User otherUser = userRepository.findById(otherUserno).orElse(null);
        Product product = productRepository.findById(room.getSellproduct()).orElse(null);

        model.addAttribute("chatroomId", chatroomId);
        model.addAttribute("userno", userno);
        model.addAttribute("messages", messages);
        model.addAttribute("otherNickname", otherUser != null ? otherUser.getNickname() : "알 수 없음");
        model.addAttribute("productTitle", product != null ? product.getTitle() : "");
        model.addAttribute("productno", room.getSellproduct());

        return "chat/chatting";
    }

    @Operation(summary = "채팅 목록")
    @GetMapping("/chat/list")
    public String chatList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        int userno = userDetails.getUserno();
        List<ChatRoomListDTO> chatrooms = chatService.getChatroomListForUser(userno);
        model.addAttribute("chatrooms", chatrooms);
        return "chat/chatList";
    }
}

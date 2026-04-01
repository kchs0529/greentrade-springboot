package com.mega.greentrade.controller;
import com.mega.greentrade.service.PayService;

import com.mega.greentrade.dto.ProductDTO;
import com.mega.greentrade.service.ProductService;
import com.mega.greentrade.security.CustomUserDetails;
import com.mega.greentrade.dto.UserDTO;
import com.mega.greentrade.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pay", description = "결제 관련 API")
@Controller
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayController {

    private final ProductService productService;
    private final UserRepository userRepository;
    private final PayService payService;

    @Operation(summary = "결제 페이지")
    @GetMapping
    public String payPage(@RequestParam int productno,
                          @AuthenticationPrincipal CustomUserDetails userDetails,
                          Model model) {
        ProductDTO product = productService.getProductInfo(productno);
        UserDTO buyer = userRepository.findById(userDetails.getUserno())
                .map(UserDTO::from).orElseThrow();
        model.addAttribute("product", product);
        model.addAttribute("buyer", buyer);
        return "pay/pay";
    }

    @Operation(summary = "결제 성공 처리")
    @GetMapping("/success")
    public String paySuccess(@RequestParam String paymentKey,
                             @RequestParam String orderId,
                             @RequestParam int amount,
                             @RequestParam int productno,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        ProductDTO product = productService.getProductInfo(productno);
        payService.confirmAndComplete(paymentKey, orderId, amount, product, userDetails.getUserno());
        return "redirect:/mypage/buylist";
    }

    @Operation(summary = "결제 실패 처리")
    @GetMapping("/fail")
    public String payFail(@RequestParam(required = false) String message,
                          @RequestParam(required = false) String code,
                          Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "pay/fail";
    }
}

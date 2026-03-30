package com.mega.greentrade.controller;
import com.mega.greentrade.service.PayService;

import com.mega.greentrade.dto.ProductDTO;
import com.mega.greentrade.service.ProductService;
import com.mega.greentrade.security.CustomUserDetails;
import com.mega.greentrade.dto.UserDTO;
import com.mega.greentrade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayController {

    private final ProductService productService;
    private final UserRepository userRepository;
    private final PayService payService;

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

    @PostMapping("/success")
    public String paySuccess(@RequestParam int productno,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        ProductDTO product = productService.getProductInfo(productno);
        payService.paySuccess(product, userDetails.getUserno());
        return "redirect:/mypage/buylist";
    }
}

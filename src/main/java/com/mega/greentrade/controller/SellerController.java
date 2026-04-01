package com.mega.greentrade.controller;

import com.mega.greentrade.service.ProductService;
import com.mega.greentrade.repository.ReviewRepository;
import com.mega.greentrade.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Seller", description = "판매자 관련 API")
@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final UserService userService;
    private final ProductService productService;
    private final ReviewRepository reviewRepository;

    @Operation(summary = "판매자 정보 조회")
    @GetMapping("/{userno}")
    public String sellerInfo(@PathVariable int userno, Model model) {
        model.addAttribute("seller", userService.getSellerInfo(userno));
        model.addAttribute("sellerItems", productService.getSellerItems(userno));
        model.addAttribute("reviews", reviewRepository.findSellerReviewSummary(userno));
        return "seller/seller_info";
    }
}

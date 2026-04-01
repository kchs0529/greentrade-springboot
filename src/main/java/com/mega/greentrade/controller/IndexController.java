package com.mega.greentrade.controller;

import com.mega.greentrade.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Index", description = "메인 페이지 API")
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ProductService productService;

    @Operation(summary = "메인 페이지")
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("recentItems", productService.getRecentItems());
        model.addAttribute("bestItems", productService.getBestList());
        return "main/index";
    }
}

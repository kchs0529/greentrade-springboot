package com.mega.greentrade.index;

import com.mega.greentrade.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ProductService productService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("recentItems", productService.getRecentItems());
        model.addAttribute("bestItems", productService.getBestList());
        return "main/index";
    }
}

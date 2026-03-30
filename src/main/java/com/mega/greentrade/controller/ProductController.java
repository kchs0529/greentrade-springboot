package com.mega.greentrade.controller;
import com.mega.greentrade.dto.ProductDTO;
import com.mega.greentrade.dto.ProductSellerProjection;
import com.mega.greentrade.dto.ProductWithUserProjection;
import com.mega.greentrade.service.ProductService;

import com.mega.greentrade.repository.HeartRepository;
import com.mega.greentrade.repository.ReviewRepository;
import com.mega.greentrade.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final HeartRepository heartRepository;
    private final ReviewRepository reviewRepository;

    @Value("${file.upload.dir}")
    private String uploadDir;

    @GetMapping("/product/add")
    public String addForm() {
        return "product/add_form";
    }

    @PostMapping("/product/add")
    public String addItem(@ModelAttribute ProductDTO dto,
                          @RequestParam(required = false) MultipartFile imageFile,
                          @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        dto.setUserno(userDetails.getUserno());
        productService.addItem(dto, imageFile, uploadDir);
        return "redirect:/";
    }

    @GetMapping("/product/detail/{productno}")
    public String detailItem(@PathVariable int productno,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model) {
        ProductDTO product = productService.getProductDetail(productno);
        ProductSellerProjection seller = productService.getSellerInfo(productno);
        model.addAttribute("product", product);
        model.addAttribute("seller", seller);

        if (userDetails != null) {
            int heartStat = heartRepository.findByUsernoAndProductno(userDetails.getUserno(), productno)
                    .map(h -> h.getLikestat()).orElse(0);
            model.addAttribute("heartStatus", heartStat);
        }

        int sellerUserno = seller != null ? seller.getUserno() : 0;
        model.addAttribute("reviews", reviewRepository.findSellerReviewSummary(sellerUserno));
        return "product/detail";
    }

    @GetMapping("/product/list")
    public String productList(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 12;
        Page<ProductWithUserProjection> productPage = productService.getProductList(page, pageSize);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "product/list";
    }

    @GetMapping("/product/share")
    public String shareList(Model model) {
        model.addAttribute("products", productService.getShareList());
        return "product/share";
    }

    @GetMapping("/product/best")
    public String bestList(Model model) {
        model.addAttribute("products", productService.getBestList());
        return "product/best";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue = "1") int page,
                         Model model) {
        if (keyword == null || keyword.isBlank()) {
            return "redirect:/";
        }
        int pageSize = 12;
        Page<ProductWithUserProjection> productPage = productService.searchByTitle(keyword, page, pageSize);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "product/search_result";
    }
}

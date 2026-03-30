package com.mega.greentrade.product;

import com.mega.greentrade.heart.HeartDAO;
import com.mega.greentrade.review.ReviewDAO;
import com.mega.greentrade.user.CustomUserDetails;
import com.mega.greentrade.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final HeartDAO heartDAO;
    private final ReviewDAO reviewDAO;

    @Value("${file.upload.dir}")
    private String uploadDir;

    // 상품 등록 폼
    @GetMapping("/product/add")
    public String addForm() {
        return "product/add_form";
    }

    // 상품 등록 처리
    @PostMapping("/product/add")
    public String addItem(@ModelAttribute ProductDTO dto,
                          @RequestParam(required = false) MultipartFile imageFile,
                          @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        dto.setUserno(userDetails.getUserno());
        productService.addItem(dto, imageFile, uploadDir);
        return "redirect:/";
    }

    // 상품 상세
    @GetMapping("/product/detail/{productno}")
    public String detailItem(@PathVariable int productno,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model) {
        ProductDTO product = productService.getProductDetail(productno);
        UserDTO seller = productService.getSellerInfo(productno);
        model.addAttribute("product", product);
        model.addAttribute("seller", seller);

        if (userDetails != null) {
            var heartStatus = heartDAO.getHeartStatus(userDetails.getUserno(), productno);
            model.addAttribute("heartStatus", heartStatus != null ? heartStatus.getLikestat() : 0);
        }

        var reviews = reviewDAO.getSellerReviewList(seller != null ? seller.getUserno() : 0);
        model.addAttribute("reviews", reviews);
        return "product/detail";
    }

    // 상품 목록
    @GetMapping("/product/list")
    public String productList(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 12;
        List<ProductDTO> products = productService.getAllProducts(page, pageSize);
        int totalCount = productService.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "product/list";
    }

    // 나눔 목록
    @GetMapping("/product/share")
    public String shareList(Model model) {
        model.addAttribute("products", productService.getShareList());
        return "product/share";
    }

    // 인기 목록
    @GetMapping("/product/best")
    public String bestList(Model model) {
        model.addAttribute("products", productService.getBestList());
        return "product/best";
    }

    // 검색
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue = "1") int page,
                         Model model) {
        if (keyword == null || keyword.isBlank()) {
            return "redirect:/";
        }
        int pageSize = 12;
        List<ProductDTO> products = productService.searchByTitle(keyword, page, pageSize);
        int totalCount = productService.getTotalCount(keyword);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "product/search_result";
    }
}

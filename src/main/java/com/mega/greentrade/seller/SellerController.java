package com.mega.greentrade.seller;

import com.mega.greentrade.product.ProductService;
import com.mega.greentrade.review.ReviewDAO;
import com.mega.greentrade.user.UserDAO;
import com.mega.greentrade.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final UserDAO userDAO;
    private final ProductService productService;
    private final ReviewDAO reviewDAO;

    @GetMapping("/{userno}")
    public String sellerInfo(@PathVariable int userno, Model model) {
        UserDTO seller = userDAO.getSellerInfo(userno);
        if (seller == null) {
            seller = userDAO.findByUserno(userno);
        }
        model.addAttribute("seller", seller);
        model.addAttribute("sellerItems", productService.getSellerItems(userno));
        model.addAttribute("reviews", reviewDAO.getSellerReviewList(userno));
        return "seller/seller_info";
    }
}

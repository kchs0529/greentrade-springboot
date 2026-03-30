package com.mega.greentrade.pay;

import com.mega.greentrade.buylist.BuyListDAO;
import com.mega.greentrade.log.LogDAO;
import com.mega.greentrade.product.ProductDAO;
import com.mega.greentrade.product.ProductDTO;
import com.mega.greentrade.selllist.SellListDAO;
import com.mega.greentrade.user.CustomUserDetails;
import com.mega.greentrade.user.UserDAO;
import com.mega.greentrade.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayController {

    private final ProductDAO productDAO;
    private final UserDAO userDAO;
    private final LogDAO logDAO;
    private final BuyListDAO buyListDAO;
    private final SellListDAO sellListDAO;

    @GetMapping
    public String payPage(@RequestParam int productno,
                          @AuthenticationPrincipal CustomUserDetails userDetails,
                          Model model) {
        ProductDTO product = productDAO.getProductInfo(productno);
        UserDTO buyer = userDAO.findByUserno(userDetails.getUserno());
        model.addAttribute("product", product);
        model.addAttribute("buyer", buyer);
        return "pay/pay";
    }

    @PostMapping("/success")
    public String paySuccess(@RequestParam int productno,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        ProductDTO product = productDAO.getProductInfo(productno);
        int buyUserno = userDetails.getUserno();

        logDAO.paySuccess(product, buyUserno);
        buyListDAO.insertBuyList(product, buyUserno);
        sellListDAO.insertSellList(product, product.getUserno());

        return "redirect:/mypage/buylist";
    }
}

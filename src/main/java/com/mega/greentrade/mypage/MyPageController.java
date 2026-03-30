package com.mega.greentrade.mypage;

import com.mega.greentrade.buylist.BuyListDAO;
import com.mega.greentrade.like.LikeDAO;
import com.mega.greentrade.log.LogDAO;
import com.mega.greentrade.log.LogDTO;
import com.mega.greentrade.product.ProductDAO;
import com.mega.greentrade.product.ProductDTO;
import com.mega.greentrade.review.ReviewDAO;
import com.mega.greentrade.selllist.SellListDAO;
import com.mega.greentrade.user.CustomUserDetails;
import com.mega.greentrade.user.UserDAO;
import com.mega.greentrade.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageDAO myPageDAO;
    private final LogDAO logDAO;
    private final BuyListDAO buyListDAO;
    private final SellListDAO sellListDAO;
    private final LikeDAO likeDAO;
    private final ReviewDAO reviewDAO;
    private final UserDAO userDAO;
    private final ProductDAO productDAO;

    @Value("${file.upload.dir}")
    private String uploadDir;

    @GetMapping
    public String myPageMain(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        MyPageDTO myPageInfo = myPageDAO.getMyPageInfo(userDetails.getUserno());
        model.addAttribute("mypage", myPageInfo);
        return "mypage/mypage_main";
    }

    @GetMapping("/edit")
    public String editPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        UserDTO user = userDAO.findByUserno(userDetails.getUserno());
        model.addAttribute("user", user);
        return "mypage/mypage_edit";
    }

    @PostMapping("/edit")
    public String editMyPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @RequestParam String email,
                             @RequestParam String user_call,
                             @RequestParam String address1,
                             @RequestParam String address2,
                             @RequestParam(required = false) MultipartFile profileImage) throws IOException {
        UserDTO user = userDAO.findByUserno(userDetails.getUserno());
        String imgurl = user.getImgurl();

        if (profileImage != null && !profileImage.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            File dest = new File(uploadDir, filename);
            dest.getParentFile().mkdirs();
            profileImage.transferTo(dest);
            imgurl = filename;
        }

        myPageDAO.updateMyPage(userDetails.getUserno(), email, user_call, address1, address2, imgurl);
        return "redirect:/mypage";
    }

    @GetMapping("/selllog")
    public String sellLog(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("sellLogs", logDAO.getSellLogList(userDetails.getUserno()));
        return "mypage/mypage_selllog";
    }

    @GetMapping("/buylog")
    public String buyLog(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("buyLogs", logDAO.getBuyLogList(userDetails.getUserno()));
        return "mypage/mypage_buylog";
    }

    @PostMapping("/selllog/delete")
    @ResponseBody
    public String deleteSellItem(@RequestParam int productno,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        logDAO.deleteSellItem(productno, userDetails.getUserno());
        return "ok";
    }

    @GetMapping("/buylist")
    public String buyList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("buyList", buyListDAO.getBuyList(userDetails.getUserno()));
        return "mypage/mypage_buylist";
    }

    @GetMapping("/selllist")
    public String sellList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("sellList", sellListDAO.getSellList(userDetails.getUserno()));
        return "mypage/mypage_selllist";
    }

    @GetMapping("/like")
    public String likeList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("likeList", likeDAO.getLikeList(userDetails.getUserno()));
        return "mypage/mypage_like";
    }

    @GetMapping("/review")
    public String reviewList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("reviews", reviewDAO.getReviewList(userDetails.getUserno()));
        return "mypage/mypage_reviewList";
    }

    @GetMapping("/review/form")
    public String reviewForm(@RequestParam int productno,
                             @RequestParam int sellerUserno,
                             Model model) {
        ProductDTO product = productDAO.getProductInfo(productno);
        UserDTO seller = userDAO.findByUserno(sellerUserno);
        model.addAttribute("product", product);
        model.addAttribute("seller", seller);
        return "mypage/mypage_reviewForm";
    }

    @PostMapping("/review/submit")
    public String submitReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @RequestParam int productno,
                               @RequestParam int sellerUserno,
                               @RequestParam String reviewcontent,
                               @RequestParam int reviewscore) {
        com.mega.greentrade.review.ReviewDTO rdto = new com.mega.greentrade.review.ReviewDTO();
        rdto.setReviewcontent(reviewcontent);
        rdto.setReviewscore(reviewscore);
        rdto.setSelleruserno(sellerUserno);

        ProductDTO product = productDAO.getProductInfo(productno);
        UserDTO seller = userDAO.findByUserno(sellerUserno);
        reviewDAO.insertReview(rdto, userDetails.getUserno(), seller, product);
        return "redirect:/mypage/buylist";
    }
}

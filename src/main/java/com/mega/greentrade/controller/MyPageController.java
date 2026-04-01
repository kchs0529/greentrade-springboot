package com.mega.greentrade.controller;
import com.mega.greentrade.dto.MyPageDTO;

import com.mega.greentrade.dto.BuyListProjection;
import com.mega.greentrade.repository.BuyListRepository;
import com.mega.greentrade.dto.LikeListProjection;
import com.mega.greentrade.repository.HeartRepository;
import com.mega.greentrade.dto.BuyLogProjection;
import com.mega.greentrade.repository.LogRepository;
import com.mega.greentrade.dto.SellLogProjection;
import com.mega.greentrade.dto.ProductDTO;
import com.mega.greentrade.service.ProductService;
import com.mega.greentrade.entity.Review;
import com.mega.greentrade.dto.ReviewDTO;
import com.mega.greentrade.repository.ReviewRepository;
import com.mega.greentrade.service.ReviewService;
import com.mega.greentrade.dto.SellListProjection;
import com.mega.greentrade.repository.SellListRepository;
import com.mega.greentrade.security.CustomUserDetails;
import com.mega.greentrade.entity.User;
import com.mega.greentrade.dto.UserDTO;
import com.mega.greentrade.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "MyPage", description = "마이페이지 관련 API")
@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final BuyListRepository buyListRepository;
    private final SellListRepository sellListRepository;
    private final HeartRepository heartRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;
    private final ProductService productService;

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Operation(summary = "마이페이지 메인")
    @GetMapping
    public String myPageMain(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        int userno = userDetails.getUserno();
        User user = userRepository.findById(userno).orElseThrow();

        MyPageDTO myPageInfo = new MyPageDTO();
        myPageInfo.setUserno(user.getUserno());
        myPageInfo.setUser_id(user.getUser_id());
        myPageInfo.setImgurl(user.getImgurl());
        myPageInfo.setUser_name(user.getUser_name());
        myPageInfo.setUser_call(user.getUser_call());
        myPageInfo.setEmail(user.getEmail());
        myPageInfo.setAddress1(user.getAddress1());
        myPageInfo.setAddress2(user.getAddress2());
        myPageInfo.setBuylistcount(buyListRepository.countByBuyuserno(userno));
        myPageInfo.setSelllistcount(sellListRepository.countBySelluserno(userno));
        myPageInfo.setReviewcount(reviewRepository.countBySelleruserno(userno));

        model.addAttribute("mypage", myPageInfo);
        return "mypage/mypage_main";
    }

    @Operation(summary = "프로필 수정 페이지")
    @GetMapping("/edit")
    public String editPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        userRepository.findById(userDetails.getUserno())
                .map(UserDTO::from)
                .ifPresent(user -> model.addAttribute("user", user));
        return "mypage/mypage_edit";
    }

    @Operation(summary = "프로필 수정 처리")
    @PostMapping("/edit")
    public String editMyPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @RequestParam String email,
                             @RequestParam String user_call,
                             @RequestParam String address1,
                             @RequestParam String address2,
                             @RequestParam(required = false) MultipartFile profileImage) throws IOException {
        User user = userRepository.findById(userDetails.getUserno()).orElseThrow();

        if (profileImage != null && !profileImage.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            File dest = new File(uploadDir, filename);
            dest.getParentFile().mkdirs();
            profileImage.transferTo(dest);
            user.setImgurl(filename);
        }

        user.setEmail(email);
        user.setUser_call(user_call);
        user.setAddress1(address1);
        user.setAddress2(address2);
        userRepository.save(user);
        return "redirect:/mypage";
    }

    @Operation(summary = "판매 로그 조회")
    @GetMapping("/selllog")
    public String sellLog(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<SellLogProjection> sellLogs = logRepository.findSellLogList(userDetails.getUserno());
        model.addAttribute("sellLogs", sellLogs);
        return "mypage/mypage_selllog";
    }

    @Operation(summary = "구매 로그 조회")
    @GetMapping("/buylog")
    public String buyLog(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<BuyLogProjection> buyLogs = logRepository.findBuyLogList(userDetails.getUserno());
        model.addAttribute("buyLogs", buyLogs);
        return "mypage/mypage_buylog";
    }

    @Operation(summary = "판매 상품 삭제")
    @PostMapping("/selllog/delete")
    @ResponseBody
    @Transactional
    public String deleteSellItem(@RequestParam int productno,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        productService.deleteSellItem(productno, userDetails.getUserno());
        return "ok";
    }

    @Operation(summary = "구매 목록 조회")
    @GetMapping("/buylist")
    public String buyList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<BuyListProjection> buyList = buyListRepository.findBuyList(userDetails.getUserno());
        model.addAttribute("buyList", buyList);
        return "mypage/mypage_buylist";
    }

    @Operation(summary = "판매 목록 조회")
    @GetMapping("/selllist")
    public String sellList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<SellListProjection> sellList = sellListRepository.findSellList(userDetails.getUserno());
        model.addAttribute("sellList", sellList);
        return "mypage/mypage_selllist";
    }

    @Operation(summary = "좋아요 목록 조회")
    @GetMapping("/like")
    public String likeList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<LikeListProjection> likeList = heartRepository.findLikeList(userDetails.getUserno());
        model.addAttribute("likeList", likeList);
        return "mypage/mypage_like";
    }

    @Operation(summary = "리뷰 목록 조회")
    @GetMapping("/review")
    public String reviewList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<Review> reviews = reviewRepository.findByReviewusernoOrderByReviewdateDesc(userDetails.getUserno());
        model.addAttribute("reviews", reviews);
        return "mypage/mypage_reviewList";
    }

    @Operation(summary = "리뷰 작성 페이지")
    @GetMapping("/review/form")
    public String reviewForm(@RequestParam int productno,
                             @RequestParam int sellerUserno,
                             Model model) {
        ProductDTO product = productService.getProductInfo(productno);
        UserDTO seller = userRepository.findById(sellerUserno).map(UserDTO::from).orElse(null);
        model.addAttribute("product", product);
        model.addAttribute("seller", seller);
        return "mypage/mypage_reviewForm";
    }

    @Operation(summary = "리뷰 제출")
    @PostMapping("/review/submit")
    public String submitReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @RequestParam int productno,
                               @RequestParam int sellerUserno,
                               @RequestParam String reviewcontent,
                               @RequestParam int reviewscore) {
        ReviewDTO rdto = new ReviewDTO();
        rdto.setReviewcontent(reviewcontent);
        rdto.setReviewscore(reviewscore);
        rdto.setSelleruserno(sellerUserno);

        ProductDTO product = productService.getProductInfo(productno);
        UserDTO seller = userRepository.findById(sellerUserno).map(UserDTO::from).orElseThrow();
        reviewService.insertReview(rdto, userDetails.getUserno(), seller, product);
        return "redirect:/mypage/buylist";
    }
}

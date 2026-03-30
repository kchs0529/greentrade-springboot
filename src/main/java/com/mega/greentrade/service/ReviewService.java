package com.mega.greentrade.service;
import com.mega.greentrade.dto.ReviewDTO;
import com.mega.greentrade.entity.Review;
import com.mega.greentrade.repository.ReviewRepository;

import com.mega.greentrade.repository.BuyListRepository;
import com.mega.greentrade.dto.ProductDTO;
import com.mega.greentrade.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BuyListRepository buyListRepository;

    @Transactional
    public void insertReview(ReviewDTO rdto, int buyerUserno, UserDTO seller, ProductDTO product) {
        Review review = new Review();
        review.setReviewcontent(rdto.getReviewcontent());
        review.setReviewscore(rdto.getReviewscore());
        review.setReviewdate(Date.valueOf(LocalDate.now()));
        review.setReviewuserno(buyerUserno);
        review.setSelleruserno(rdto.getSelleruserno());
        review.setSellername(seller.getUser_name());
        review.setPaymethod(product.getPaymethod());
        reviewRepository.save(review);

        buyListRepository.markAsReviewed(product.getProductno(), buyerUserno);
    }
}

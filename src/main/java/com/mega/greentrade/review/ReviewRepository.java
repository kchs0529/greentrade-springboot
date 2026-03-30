package com.mega.greentrade.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByReviewusernoOrderByReviewdateDesc(int reviewuserno);

    // 판매자별 리뷰 집계
    @Query(value = """
            SELECT reviewcontent, COUNT(*) AS content_count
            FROM review
            WHERE selleruserno = :sellerUserno
            GROUP BY reviewcontent
            """, nativeQuery = true)
    List<SellerReviewProjection> findSellerReviewSummary(@Param("sellerUserno") int sellerUserno);
}

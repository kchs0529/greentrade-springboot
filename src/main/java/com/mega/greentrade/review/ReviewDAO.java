package com.mega.greentrade.review;

import com.mega.greentrade.product.ProductDTO;
import com.mega.greentrade.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewDAO {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public int insertReview(ReviewDTO rdto, int userno, UserDTO sellerDto, ProductDTO pdto) {
        String insertSql = "INSERT INTO review VALUES(seq_reviewno.nextval,?,?,sysdate,?,?,?,?)";
        int result = jdbcTemplate.update(insertSql,
                rdto.getReviewcontent(), rdto.getReviewscore(),
                userno, rdto.getSelleruserno(),
                sellerDto.getUser_name(), pdto.getPaymethod());

        if (result > 0) {
            return jdbcTemplate.update(
                "UPDATE buylist SET isreview='yes' WHERE productno=? AND buyuserno=?",
                pdto.getProductno(), userno);
        }
        return 0;
    }

    public List<ReviewDTO> getReviewList(int userno) {
        String sql = "SELECT * FROM review WHERE reviewuserno=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReviewDTO dto = new ReviewDTO();
            dto.setReviewno(rs.getInt("reviewno"));
            dto.setReviewcontent(rs.getString("reviewcontent"));
            dto.setReviewscore(rs.getInt("reviewscore"));
            dto.setReviewdate(rs.getDate("reviewdate"));
            dto.setReviewuserno(rs.getInt("reviewuserno"));
            dto.setSellername(rs.getString("sellername"));
            dto.setPaymethod(rs.getString("paymethod"));
            return dto;
        }, userno);
    }

    public List<ReviewDTO> getSellerReviewList(int sellerUserno) {
        String sql = "SELECT reviewcontent, COUNT(*) AS content_count"
                   + " FROM review WHERE selleruserno=?"
                   + " GROUP BY reviewcontent";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReviewDTO dto = new ReviewDTO();
            dto.setReviewcontent(rs.getString("reviewcontent"));
            dto.setContent_count(rs.getInt("content_count"));
            return dto;
        }, sellerUserno);
    }
}

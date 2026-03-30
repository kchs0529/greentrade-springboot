package com.mega.greentrade.mypage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyPageDAO {

    private final JdbcTemplate jdbcTemplate;

    public MyPageDTO getMyPageInfo(int userno) {
        String sql = "SELECT u.userno, u.user_id, u.imgurl, u.user_name, u.user_call, u.email, u.address1, u.address2,"
                   + " (SELECT COUNT(*) FROM buylist bl WHERE u.userno = bl.buyuserno) AS buylistcount,"
                   + " (SELECT COUNT(*) FROM selllist sl WHERE u.userno = sl.selluserno) AS selllistcount,"
                   + " (SELECT COUNT(*) FROM review r WHERE u.userno = r.reviewuserno) AS reviewcount"
                   + " FROM user_table_real u WHERE u.userno=?";
        List<MyPageDTO> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            MyPageDTO dto = new MyPageDTO();
            dto.setUserno(rs.getInt("userno"));
            dto.setUser_id(rs.getString("user_id"));
            dto.setImgurl(rs.getString("imgurl"));
            dto.setUser_name(rs.getString("user_name"));
            dto.setUser_call(rs.getString("user_call"));
            dto.setEmail(rs.getString("email"));
            dto.setAddress1(rs.getString("address1"));
            dto.setAddress2(rs.getString("address2"));
            dto.setBuylistcount(rs.getInt("buylistcount"));
            dto.setSelllistcount(rs.getInt("selllistcount"));
            dto.setReviewcount(rs.getInt("reviewcount"));
            return dto;
        }, userno);
        return list.isEmpty() ? null : list.get(0);
    }

    public void updateMyPage(int userno, String email, String userCall, String address1, String address2, String imgurl) {
        jdbcTemplate.update(
            "UPDATE user_table_real SET email=?, user_call=?, address1=?, address2=?, imgurl=? WHERE userno=?",
            email, userCall, address1, address2, imgurl, userno);
    }
}

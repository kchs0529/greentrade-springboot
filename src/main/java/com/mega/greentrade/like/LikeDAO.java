package com.mega.greentrade.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeDAO {

    private final JdbcTemplate jdbcTemplate;

    public List<LikeDTO> getLikeList(int userno) {
        String sql = "SELECT l.likeno, l.likedate, l.userno, l.likestat,"
                   + " p.productno, p.title, p.price, p.image, u.user_name, p.productstatus"
                   + " FROM product p"
                   + " JOIN heart l ON p.productno = l.productno"
                   + " JOIN user_table_real u ON p.userno = u.userno"
                   + " WHERE l.userno=?"
                   + " ORDER BY l.likedate DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            LikeDTO dto = new LikeDTO();
            dto.setLikeno(rs.getInt("likeno"));
            dto.setLikedate(rs.getDate("likedate"));
            dto.setLikestat(rs.getInt("likestat"));
            dto.setUserno(rs.getInt("userno"));
            dto.setProductno(rs.getInt("productno"));
            dto.setTitle(rs.getString("title"));
            dto.setPrice(rs.getString("price"));
            dto.setImage(rs.getString("image"));
            dto.setUser_name(rs.getString("user_name"));
            dto.setProductstatus(rs.getString("productstatus"));
            return dto;
        }, userno);
    }
}

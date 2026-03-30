package com.mega.greentrade.heart;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HeartDAO {

    private final JdbcTemplate jdbcTemplate;

    public HeartDTO getHeartStatus(int userno, int productno) {
        String sql = "SELECT * FROM heart WHERE userno=? AND productno=?";
        List<HeartDTO> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            HeartDTO dto = new HeartDTO();
            dto.setLikestat(rs.getInt("likestat"));
            dto.setUserno(rs.getInt("userno"));
            dto.setProductno(rs.getInt("productno"));
            return dto;
        }, userno, productno);
        return list.isEmpty() ? null : list.get(0);
    }

    public int doHeart(int userno, int productno) {
        String sql = "INSERT INTO heart (likeno, likedate, likestat, userno, productno)"
                   + " SELECT like_no_seq.nextval, sysdate, 1, ?, ?"
                   + " FROM dual"
                   + " WHERE NOT EXISTS ("
                   + "   SELECT 1 FROM heart WHERE likestat=1 AND userno=? AND productno=?)";
        return jdbcTemplate.update(sql, userno, productno, userno, productno);
    }

    public int cancleHeart(int userno, int productno) {
        return jdbcTemplate.update("DELETE FROM heart WHERE productno=? AND userno=?", productno, userno);
    }
}

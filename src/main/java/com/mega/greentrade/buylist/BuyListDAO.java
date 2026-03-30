package com.mega.greentrade.buylist;

import com.mega.greentrade.product.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BuyListDAO {

    private final JdbcTemplate jdbcTemplate;

    public int insertBuyList(ProductDTO pdto, int userno) {
        return jdbcTemplate.update(
            "INSERT INTO buylist VALUES(seq_buylistno.nextval, sysdate, 'no', ?, ?, ?)",
            pdto.getProductno(), userno, pdto.getUserno());
    }

    public List<BuyListDTO> getBuyList(int userno) {
        String sql = "SELECT bl.buylistdate, bl.isreview, p.image, p.productno, p.title, p.price, p.paymethod,"
                   + " us.user_name AS buyusername,"
                   + " (SELECT user_name FROM user_table_real WHERE userno = bl.selluserno) AS sellername"
                   + " FROM buylist bl"
                   + " JOIN product p ON bl.productno = p.productno"
                   + " JOIN user_table_real us ON bl.buyuserno = us.userno"
                   + " WHERE bl.buyuserno=?"
                   + " ORDER BY bl.buylistdate DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BuyListDTO dto = new BuyListDTO();
            dto.setBuylistdate(rs.getDate("buylistdate"));
            dto.setTitle(rs.getString("title"));
            dto.setImage(rs.getString("image"));
            dto.setPrice(rs.getString("price"));
            dto.setPaymethod(rs.getString("paymethod"));
            dto.setUser_name(rs.getString("sellername"));
            dto.setProductno(rs.getInt("productno"));
            dto.setIsreview(rs.getString("isreview"));
            return dto;
        }, userno);
    }
}

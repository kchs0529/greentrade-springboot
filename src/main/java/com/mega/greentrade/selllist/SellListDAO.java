package com.mega.greentrade.selllist;

import com.mega.greentrade.product.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SellListDAO {

    private final JdbcTemplate jdbcTemplate;

    public int insertSellList(ProductDTO pdto, int userno) {
        return jdbcTemplate.update(
            "INSERT INTO selllist VALUES(seq_buylistno.nextval, sysdate, ?, ?, ?)",
            pdto.getProductno(), userno, pdto.getUserno());
    }

    public List<SellListDTO> getSellList(int userno) {
        String sql = "SELECT sl.selllistdate, p.image, p.productno, p.title, p.price, p.paymethod,"
                   + " us.user_name AS sellusername,"
                   + " (SELECT user_name FROM user_table_real WHERE userno = sl.buyuserno) AS buyername"
                   + " FROM selllist sl"
                   + " JOIN product p ON sl.productno = p.productno"
                   + " JOIN user_table_real us ON sl.selluserno = us.userno"
                   + " WHERE sl.selluserno=?"
                   + " ORDER BY sl.selllistdate DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            SellListDTO dto = new SellListDTO();
            dto.setSelllistdate(rs.getDate("selllistdate"));
            dto.setTitle(rs.getString("title"));
            dto.setImage(rs.getString("image"));
            dto.setPrice(rs.getString("price"));
            dto.setPaymethod(rs.getString("paymethod"));
            dto.setUser_name(rs.getString("buyername"));
            dto.setProductno(rs.getInt("productno"));
            return dto;
        }, userno);
    }
}

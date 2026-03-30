package com.mega.greentrade.log;

import com.mega.greentrade.product.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LogDAO {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<LogDTO> logRowMapper = (rs, rowNum) -> {
        LogDTO dto = new LogDTO();
        dto.setProductno(rs.getInt("productno"));
        dto.setTradestatus(rs.getString("tradestatus"));
        dto.setTitle(rs.getString("title"));
        dto.setPrice(rs.getString("price"));
        dto.setPaymethod(rs.getString("paymethod"));
        dto.setTrademethod(rs.getString("trademethod"));
        dto.setBuyuserno(rs.getInt("buyuserno"));
        try { dto.setTradestartdate(rs.getDate("tradestartdate")); } catch (Exception ignored) {}
        try { dto.setTradesuccessdate(rs.getDate("tradesuccessdate")); } catch (Exception ignored) {}
        return dto;
    };

    public List<LogDTO> getSellLogList(int userno) {
        String sql = "SELECT logs.tradestatus, p.title, p.price, logs.tradestartdate, p.paymethod, p.trademethod, p.productno, logs.buyuserno"
                   + " FROM product p JOIN logs ON p.productno = logs.productno"
                   + " WHERE logs.selluserno=? AND logs.tradetype='판매'"
                   + " ORDER BY logs.tradestartdate DESC";
        return jdbcTemplate.query(sql, logRowMapper, userno);
    }

    public List<LogDTO> getBuyLogList(int userno) {
        String sql = "SELECT logs.tradestatus, p.title, p.price, logs.tradesuccessdate, p.paymethod, p.trademethod, logs.buyuserno, p.productno"
                   + " FROM product p JOIN logs ON p.productno = logs.productno"
                   + " WHERE logs.buyuserno=? AND logs.tradetype='구매' AND p.sellstatus='판매완료'"
                   + " ORDER BY logs.tradestartdate DESC";
        return jdbcTemplate.query(sql, logRowMapper, userno);
    }

    @Transactional
    public int paySuccess(ProductDTO pdto, int buyUserno) {
        jdbcTemplate.update(
            "UPDATE logs SET tradestatus='거래완료', tradesuccessdate=SYSDATE, buyuserno=? WHERE productno=? AND tradetype='판매'",
            buyUserno, pdto.getProductno());

        jdbcTemplate.update(
            "INSERT INTO logs VALUES(seq_tradeno.nextval, SYSDATE, SYSDATE, '구매', '택배거래', '거래완료', ?, ?, ?)",
            pdto.getProductno(), buyUserno, pdto.getUserno());

        return jdbcTemplate.update(
            "UPDATE product SET sellstatus='판매완료' WHERE productno=?",
            pdto.getProductno());
    }

    @Transactional
    public void deleteSellItem(int productno, int userno) {
        jdbcTemplate.update("DELETE FROM chatroom WHERE sellproduct=?", productno);
        jdbcTemplate.update("DELETE FROM heart WHERE productno=?", productno);
        jdbcTemplate.update("DELETE FROM product WHERE productno=? AND userno=?", productno, userno);
        jdbcTemplate.update("DELETE FROM logs WHERE productno=? AND selluserno=?", productno, userno);
    }

    public void deleteLogs(int productno) {
        jdbcTemplate.update("DELETE FROM logs WHERE productno=?", productno);
    }
}

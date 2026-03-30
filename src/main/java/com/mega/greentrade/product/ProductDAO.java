package com.mega.greentrade.product;

import com.mega.greentrade.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductDAO {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ProductDTO> productRowMapper = (rs, rowNum) -> {
        ProductDTO dto = new ProductDTO();
        dto.setProductno(rs.getInt("productno"));
        dto.setTitle(rs.getString("title"));
        dto.setPrice(rs.getString("price"));
        dto.setImage(rs.getString("image"));
        dto.setPaymethod(rs.getString("paymethod"));
        dto.setUserno(rs.getInt("userno"));
        dto.setProductStatus(rs.getString("productstatus"));
        dto.setProductDetail(rs.getString("productdetail"));
        dto.setTrademethod(rs.getString("trademethod"));
        dto.setSellstatus(rs.getString("sellstatus"));
        try { dto.setAdddate(rs.getDate("adddate")); } catch (Exception ignored) {}
        try { dto.setViewcount(rs.getInt("viewcount")); } catch (Exception ignored) {}
        try { dto.setTradelocation(rs.getString("tradelocation")); } catch (Exception ignored) {}
        try { dto.setUser_name(rs.getString("user_name")); } catch (Exception ignored) {}
        return dto;
    };

    public int insertItem(ProductDTO dto) {
        String insertItemQuery = "INSERT INTO product VALUES(seq_productno.nextval,?,?,?,?,?,?,?,'판매중',sysdate,null,0,?,?,?)";
        String logsQuery = "INSERT INTO logs VALUES(seq_tradeno.nextval,sysdate,null,'판매',?,'거래중',?,null,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertItemQuery, new String[]{"productno"});
            ps.setString(1, dto.getTitle());
            ps.setString(2, dto.getPrice());
            ps.setInt(3, dto.getLikenum());
            ps.setString(4, dto.getProductStatus());
            ps.setString(5, dto.getProductDetail());
            ps.setString(6, dto.getPaymethod());
            ps.setString(7, dto.getTrademethod());
            ps.setInt(8, dto.getUserno());
            ps.setString(9, dto.getImage());
            ps.setString(10, dto.getTradelocation());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            int productno = key.intValue();
            jdbcTemplate.update(logsQuery, dto.getTrademethod(), productno, dto.getUserno());
            return productno;
        }
        return 0;
    }

    public ProductDTO getProductInfo(int productno) {
        String sql = "SELECT * FROM product WHERE productno=?";
        List<ProductDTO> list = jdbcTemplate.query(sql, productRowMapper, productno);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<ProductDTO> getRecentItems() {
        String sql = "SELECT * FROM ("
                   + " SELECT p.productno, p.title, p.price, p.image, u.user_name, p.productstatus, p.trademethod, p.adddate, p.paymethod, p.sellstatus, p.userno, p.productdetail, p.tradelocation, p.viewcount"
                   + " FROM product p JOIN user_table_real u ON p.userno = u.userno"
                   + " WHERE p.sellstatus = '판매중'"
                   + " ORDER BY p.adddate DESC"
                   + ") WHERE ROWNUM <= 8";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    public List<ProductDTO> getAllProducts(int startRow, int endRow) {
        String sql = "SELECT * FROM ("
                   + " SELECT ROWNUM AS rnum, p.*, u.user_name FROM ("
                   + "   SELECT productno, title, price, productstatus, image, userno, sellstatus, trademethod, paymethod, productdetail, adddate, viewcount, tradelocation FROM product"
                   + " ) p LEFT JOIN user_table_real u ON p.userno = u.userno"
                   + " WHERE ROWNUM <= ?"
                   + ") WHERE rnum >= ?";
        return jdbcTemplate.query(sql, productRowMapper, endRow, startRow);
    }

    public List<ProductDTO> getProductList() {
        String sql = "SELECT * FROM ("
                   + " SELECT p.productno, p.title, p.price, p.image, u.user_name, p.productstatus, p.trademethod, p.adddate, p.paymethod, p.sellstatus, p.userno, p.productdetail, p.tradelocation, p.viewcount"
                   + " FROM product p JOIN user_table_real u ON p.userno = u.userno"
                   + " WHERE p.sellstatus = '판매중'"
                   + " ORDER BY p.adddate DESC"
                   + ") WHERE ROWNUM <= 16";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    public List<ProductDTO> getShareList() {
        String sql = "SELECT * FROM ("
                   + " SELECT p.productno, p.title, p.price, p.image, u.user_name, p.productstatus, p.trademethod, p.adddate, p.paymethod, p.sellstatus, p.userno, p.productdetail, p.tradelocation, p.viewcount"
                   + " FROM product p JOIN user_table_real u ON p.userno = u.userno"
                   + " WHERE p.sellstatus = '판매중' AND p.paymethod = '나눔'"
                   + " ORDER BY p.adddate DESC"
                   + ") WHERE ROWNUM <= 16";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    public List<ProductDTO> getBestList() {
        String sql = "SELECT * FROM ("
                   + " SELECT p.productno, p.title, p.price, p.image, u.user_name, p.productstatus, p.trademethod, p.adddate, p.paymethod, p.sellstatus, p.userno, p.productdetail, p.tradelocation, p.viewcount"
                   + " FROM product p JOIN user_table_real u ON p.userno = u.userno"
                   + " WHERE p.sellstatus = '판매중'"
                   + " ORDER BY p.viewcount DESC"
                   + ") WHERE ROWNUM <= 16";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    public List<ProductDTO> searchByTitle(String title, int startRow, int endRow) {
        String sql = "SELECT * FROM ("
                   + " SELECT ROWNUM AS rnum, p.*, u.user_name FROM ("
                   + "   SELECT productno, title, price, productstatus, image, userno, sellstatus, trademethod, paymethod, productdetail, adddate, viewcount, tradelocation FROM product"
                   + "   WHERE title LIKE ?"
                   + " ) p LEFT JOIN user_table_real u ON p.userno = u.userno"
                   + " WHERE ROWNUM <= ?"
                   + ") WHERE rnum >= ?";
        return jdbcTemplate.query(sql, productRowMapper, "%" + title + "%", endRow, startRow);
    }

    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM product";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    public int getTotalCount(String title) {
        String sql = "SELECT COUNT(*) FROM product WHERE title LIKE ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, "%" + title + "%");
        return count != null ? count : 0;
    }

    public void increaseViewCount(int productno) {
        jdbcTemplate.update("UPDATE product SET viewcount = viewcount + 1 WHERE productno=?", productno);
    }

    public UserDTO getSellerInfo(int productno) {
        String sql = "SELECT u.nickname, u.greenscore, u.userno, u.imgurl"
                   + " FROM product p JOIN user_table_real u ON p.userno = u.userno"
                   + " WHERE productno=?";
        List<UserDTO> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserDTO dto = new UserDTO();
            dto.setNickname(rs.getString("nickname"));
            dto.setGreenscore(rs.getInt("greenscore"));
            dto.setUserno(rs.getInt("userno"));
            dto.setImgurl(rs.getString("imgurl"));
            return dto;
        }, productno);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<ProductDTO> getSellerItems(int userno) {
        String sql = "SELECT productno, title, price, likenum, productstatus, trademethod, adddate, paymethod, image, userno, sellstatus, productdetail, tradelocation, viewcount"
                   + " FROM product WHERE userno=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ProductDTO dto = new ProductDTO();
            dto.setProductno(rs.getInt("productno"));
            dto.setTitle(rs.getString("title"));
            dto.setPrice(rs.getString("price"));
            dto.setLikenum(rs.getInt("likenum"));
            dto.setProductStatus(rs.getString("productstatus"));
            dto.setTrademethod(rs.getString("trademethod"));
            dto.setAdddate(rs.getDate("adddate"));
            dto.setPaymethod(rs.getString("paymethod"));
            dto.setImage(rs.getString("image"));
            return dto;
        }, userno);
    }

    public void deleteProduct(int productno) {
        jdbcTemplate.update("DELETE FROM chatroom WHERE sellproduct=?", productno);
        jdbcTemplate.update("DELETE FROM heart WHERE productno=?", productno);
        jdbcTemplate.update("DELETE FROM product WHERE productno=?", productno);
    }

    public void managerDeleteProduct(int productno) {
        deleteProduct(productno);
    }
}

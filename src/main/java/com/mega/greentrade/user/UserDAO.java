package com.mega.greentrade.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDAO {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<UserDTO> userRowMapper = (rs, rowNum) -> {
        UserDTO dto = new UserDTO();
        dto.setUserno(rs.getInt("userno"));
        dto.setUser_name(rs.getString("user_name"));
        dto.setUser_call(rs.getString("user_call"));
        dto.setUser_id(rs.getString("user_id"));
        dto.setUser_password(rs.getString("user_password"));
        dto.setEmail(rs.getString("email"));
        dto.setNickname(rs.getString("nickname"));
        dto.setGreenscore(rs.getInt("greenscore"));
        dto.setSellcount(rs.getInt("sellcount"));
        dto.setStaff(rs.getString("staff"));
        dto.setPostnum(rs.getString("postnum"));
        dto.setImgurl(rs.getString("imgurl"));
        dto.setAddress1(rs.getString("address1"));
        dto.setAddress2(rs.getString("address2"));
        return dto;
    };

    public UserDTO findByIdAndPassword(String userId, String password) {
        String sql = "SELECT * FROM user_table_real WHERE user_id = ? AND user_password = ?";
        List<UserDTO> list = jdbcTemplate.query(sql, userRowMapper, userId, password);
        return list.isEmpty() ? null : list.get(0);
    }

    public UserDTO findById(String userId) {
        String sql = "SELECT * FROM user_table_real WHERE user_id = ?";
        List<UserDTO> list = jdbcTemplate.query(sql, userRowMapper, userId);
        return list.isEmpty() ? null : list.get(0);
    }

    public UserDTO findByUserno(int userno) {
        String sql = "SELECT * FROM user_table_real WHERE userno = ?";
        List<UserDTO> list = jdbcTemplate.query(sql, userRowMapper, userno);
        return list.isEmpty() ? null : list.get(0);
    }

    public UserDTO findByEmailAndCall(String email, String userCall) {
        String sql = "SELECT * FROM user_table_real WHERE email = ? AND user_call = ?";
        List<UserDTO> list = jdbcTemplate.query(sql, userRowMapper, email, userCall);
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean existsById(String userId) {
        String sql = "SELECT COUNT(*) FROM user_table_real WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    public int insertUser(JoinDTO dto) {
        String sql = "INSERT INTO user_table_real(userno, user_name, user_call, user_id, user_password, email, nickname, imgurl, staff, address1, address2, postnum)"
                   + " VALUES(user_no_add.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getUser_name(), dto.getUser_call(), dto.getUser_id(),
                dto.getUser_password(), dto.getEmail(), dto.getNickname(),
                dto.getImgurl(), dto.getStaff(),
                dto.getAddress1(), dto.getAddress2(), dto.getPostnum());
    }

    public int insertAddress(JoinDTO dto) {
        String sql = "INSERT INTO address(addno, receivername, phone, address1, address2, postnum, userid)"
                   + " VALUES(add_no_seq.nextval, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getUser_name(), dto.getUser_call(),
                dto.getAddress1(), dto.getAddress2(),
                dto.getPostnum(), dto.getUser_id());
    }

    public int updatePassword(String userId, String newPassword) {
        String sql = "UPDATE user_table_real SET user_password = ? WHERE user_id = ?";
        return jdbcTemplate.update(sql, newPassword, userId);
    }

    public UserDTO getSellerInfo(int userno) {
        String sql = "SELECT u.userno, u.nickname, u.imgurl, u.greenscore,"
                   + " COUNT(*) AS reviewCount FROM review r"
                   + " JOIN user_table_real u ON r.selleruserno = u.userno"
                   + " WHERE r.selleruserno = ?"
                   + " GROUP BY u.userno, u.nickname, u.imgurl, u.greenscore";
        List<UserDTO> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserDTO dto = new UserDTO();
            dto.setUserno(rs.getInt("userno"));
            dto.setNickname(rs.getString("nickname"));
            dto.setGreenscore(rs.getInt("greenscore"));
            dto.setImgurl(rs.getString("imgurl"));
            dto.setReviewCount(rs.getInt("reviewCount"));
            return dto;
        }, userno);
        return list.isEmpty() ? null : list.get(0);
    }

    public int updateUserInfo(UserDTO dto) {
        String sql = "UPDATE user_table_real SET user_name=?, user_call=?, email=?, nickname=?, address1=?, address2=?, postnum=?"
                   + " WHERE userno=?";
        return jdbcTemplate.update(sql,
                dto.getUser_name(), dto.getUser_call(), dto.getEmail(),
                dto.getNickname(), dto.getAddress1(), dto.getAddress2(),
                dto.getPostnum(), dto.getUserno());
    }

    public int updateProfileImage(int userno, String imgurl) {
        String sql = "UPDATE user_table_real SET imgurl=? WHERE userno=?";
        return jdbcTemplate.update(sql, imgurl, userno);
    }

    public List<UserDTO> findAllUsers() {
        String sql = "SELECT * FROM user_table_real ORDER BY userno";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public int deleteUser(int userno) {
        String sql = "DELETE FROM user_table_real WHERE userno=?";
        return jdbcTemplate.update(sql, userno);
    }
}

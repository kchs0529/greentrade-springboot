package com.mega.greentrade.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ManagerDAO {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ManagerDTO> managerRowMapper = (rs, rowNum) -> {
        ManagerDTO dto = new ManagerDTO();
        dto.setUserno(rs.getInt("userno"));
        dto.setUser_name(rs.getString("user_name"));
        dto.setUser_id(rs.getString("user_id"));
        try { dto.setTargetid_count(rs.getInt("targetid_count")); } catch (Exception ignored) {}
        try { dto.setEmail(rs.getString("email")); } catch (Exception ignored) {}
        try { dto.setUser_call(rs.getString("user_call")); } catch (Exception ignored) {}
        try { dto.setAddress1(rs.getString("address1")); } catch (Exception ignored) {}
        try { dto.setAddress2(rs.getString("address2")); } catch (Exception ignored) {}
        try { dto.setImgurl(rs.getString("imgurl")); } catch (Exception ignored) {}
        try { dto.setUser_password(rs.getString("user_password")); } catch (Exception ignored) {}
        return dto;
    };

    public List<ManagerDTO> getMemberList(int startRow, int endRow) {
        String sql = "SELECT * FROM (SELECT ROWNUM AS rnum, rr.* FROM ("
                   + " SELECT u.userno, u.user_name, u.user_id, COUNT(r.targetid) AS targetid_count"
                   + " FROM user_table_real u LEFT JOIN report r ON u.userno = r.targetid"
                   + " WHERE u.staff='user'"
                   + " GROUP BY u.userno, u.user_name, u.user_id"
                   + " ORDER BY u.userno DESC"
                   + ") rr) WHERE rnum BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, managerRowMapper, startRow, endRow);
    }

    public ManagerDTO getMemberInfo(String userId) {
        String sql = "SELECT userno, user_id, user_password, user_name, email, user_call, address1, address2, imgurl"
                   + " FROM user_table_real WHERE user_id=?";
        List<ManagerDTO> list = jdbcTemplate.query(sql, managerRowMapper, userId);
        return list.isEmpty() ? null : list.get(0);
    }

    public void deleteMember(String userId, int userno) {
        jdbcTemplate.update("DELETE FROM heart WHERE userno=?", userno);
        jdbcTemplate.update("DELETE FROM user_table_real WHERE user_id=?", userId);
    }

    public int getTotalMemberCount() {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM user_table_real WHERE staff='user'", Integer.class);
        return count != null ? count : 0;
    }
}

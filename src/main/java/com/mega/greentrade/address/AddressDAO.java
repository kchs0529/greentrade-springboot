package com.mega.greentrade.address;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AddressDAO {

    private final JdbcTemplate jdbcTemplate;

    public int updateAddress(AddressDTO dto) {
        return jdbcTemplate.update(
            "UPDATE address SET receivername=?, phone=?, address1=?, address2=?, postnum=? WHERE userid=?",
            dto.getReceivername(), dto.getPhone(),
            dto.getAddress1(), dto.getAddress2(),
            dto.getPostnum(), dto.getUserid());
    }

    public AddressDTO getAddressByUserId(String userId) {
        String sql = "SELECT * FROM address WHERE userid=?";
        List<AddressDTO> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            AddressDTO dto = new AddressDTO();
            dto.setReceivername(rs.getString("receivername"));
            dto.setPhone(rs.getString("phone"));
            dto.setAddress1(rs.getString("address1"));
            dto.setAddress2(rs.getString("address2"));
            dto.setPostnum(rs.getString("postnum"));
            dto.setUserid(rs.getString("userid"));
            return dto;
        }, userId);
        return list.isEmpty() ? null : list.get(0);
    }
}

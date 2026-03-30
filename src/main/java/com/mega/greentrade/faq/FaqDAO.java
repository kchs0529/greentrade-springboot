package com.mega.greentrade.faq;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FaqDAO {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<FaqDTO> faqRowMapper = (rs, rowNum) -> {
        FaqDTO dto = new FaqDTO();
        dto.setFaqno(rs.getInt("faqno"));
        dto.setFaqtitle(rs.getString("faqtitle"));
        dto.setFaqcontent(rs.getString("faqcontent"));
        dto.setFaqdate(rs.getDate("faqdate"));
        dto.setFaqcate(rs.getString("faqcate"));
        return dto;
    };

    public List<FaqDTO> getFaqList(int startRow, int endRow) {
        String sql = "SELECT * FROM ("
                   + " SELECT ROWNUM AS rnum, t.* FROM ("
                   + "   SELECT * FROM faq ORDER BY faqno DESC"
                   + " ) t"
                   + ") WHERE rnum BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, faqRowMapper, startRow, endRow);
    }

    public FaqDTO getFaqContent(int faqno) {
        String sql = "SELECT * FROM faq WHERE faqno=?";
        List<FaqDTO> list = jdbcTemplate.query(sql, faqRowMapper, faqno);
        return list.isEmpty() ? null : list.get(0);
    }

    public void saveFaq(FaqDTO dto) {
        jdbcTemplate.update(
            "INSERT INTO faq VALUES(faq_sequence.NEXTVAL, ?, ?, sysdate, ?)",
            dto.getFaqtitle(), dto.getFaqcontent(), dto.getFaqcate());
    }

    public int getTotalCount() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM faq", Integer.class);
        return count != null ? count : 0;
    }
}

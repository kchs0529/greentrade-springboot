package com.mega.greentrade.report;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportDAO {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ReportDTO> reportRowMapper = (rs, rowNum) -> {
        ReportDTO dto = new ReportDTO();
        dto.setReportid(rs.getInt("reportid"));
        dto.setReporterid(rs.getInt("reporterid"));
        dto.setReporterNickname(rs.getString("reporter_nickname"));
        dto.setTargetid(rs.getInt("targetid"));
        dto.setTargetNickname(rs.getString("target_nickname"));
        dto.setReportdate(rs.getDate("reportdate"));
        dto.setReportimgurl(rs.getString("reportimgurl"));
        dto.setReportcontent(rs.getString("reportcontent"));
        dto.setReporttitle(rs.getString("reporttitle"));
        return dto;
    };

    private static final String REPORT_BASE_SELECT =
        "SELECT r.*, u1.nickname AS reporter_nickname, u2.nickname AS target_nickname"
        + " FROM report r"
        + " JOIN user_table_real u1 ON r.reporterid = u1.userno"
        + " JOIN user_table_real u2 ON r.targetid = u2.userno";

    public ReportDTO getReportContent(int reportid) {
        String sql = REPORT_BASE_SELECT + " WHERE r.reportid=?";
        List<ReportDTO> list = jdbcTemplate.query(sql, reportRowMapper, reportid);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<ReportDTO> getReportList(int startRow, int endRow) {
        String sql = "SELECT * FROM (SELECT ROWNUM AS rnum, rr.* FROM ("
                   + REPORT_BASE_SELECT + " ORDER BY r.reportid DESC"
                   + ") rr) WHERE rnum BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, reportRowMapper, startRow, endRow);
    }

    public List<ReportDTO> getReportListByTarget(int userno, int startRow, int endRow) {
        String sql = "SELECT * FROM (SELECT ROWNUM AS rnum, rr.* FROM ("
                   + REPORT_BASE_SELECT + " WHERE r.targetid=? ORDER BY r.reportid DESC"
                   + ") rr) WHERE rnum BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, reportRowMapper, userno, startRow, endRow);
    }

    public void saveReport(ReportDTO dto, int reporterid) {
        String sql = "INSERT INTO report (reportid, reporterid, targetid, reportdate, reportimgurl, reportcontent, reporttitle)"
                   + " VALUES(report_seq.NEXTVAL, ?, (SELECT userno FROM user_table_real WHERE nickname=?), sysdate, ?, ?, ?)";
        jdbcTemplate.update(sql,
                reporterid, dto.getTargetNickname(),
                dto.getReportimgurl(), dto.getReportcontent(), dto.getReporttitle());
    }

    public int getTotalCount() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM report", Integer.class);
        return count != null ? count : 0;
    }

    public int getTotalCountByTarget(int userno) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM report WHERE targetid=?", Integer.class, userno);
        return count != null ? count : 0;
    }
}

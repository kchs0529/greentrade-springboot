package com.mega.greentrade.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query(value = """
            SELECT r.reportid, r.reporterid, r.targetid, r.reportdate,
                   r.reportimgurl, r.reportcontent, r.reporttitle,
                   u1.nickname AS reporterNickname, u2.nickname AS targetNickname
            FROM report r
            JOIN user_table_real u1 ON r.reporterid = u1.userno
            JOIN user_table_real u2 ON r.targetid = u2.userno
            ORDER BY r.reportid DESC
            """,
           countQuery = "SELECT COUNT(*) FROM report",
           nativeQuery = true)
    Page<ReportDetailProjection> findAllReports(Pageable pageable);

    @Query(value = """
            SELECT r.reportid, r.reporterid, r.targetid, r.reportdate,
                   r.reportimgurl, r.reportcontent, r.reporttitle,
                   u1.nickname AS reporterNickname, u2.nickname AS targetNickname
            FROM report r
            JOIN user_table_real u1 ON r.reporterid = u1.userno
            JOIN user_table_real u2 ON r.targetid = u2.userno
            WHERE r.targetid = :userno
            ORDER BY r.reportid DESC
            """,
           countQuery = "SELECT COUNT(*) FROM report WHERE targetid = :userno",
           nativeQuery = true)
    Page<ReportDetailProjection> findReportsByTarget(@Param("userno") int userno, Pageable pageable);

    @Query(value = """
            SELECT r.reportid, r.reporterid, r.targetid, r.reportdate,
                   r.reportimgurl, r.reportcontent, r.reporttitle,
                   u1.nickname AS reporterNickname, u2.nickname AS targetNickname
            FROM report r
            JOIN user_table_real u1 ON r.reporterid = u1.userno
            JOIN user_table_real u2 ON r.targetid = u2.userno
            WHERE r.reportid = :reportid
            """, nativeQuery = true)
    ReportDetailProjection findReportDetail(@Param("reportid") int reportid);

    @Query(value = """
            INSERT INTO report (reportid, reporterid, targetid, reportdate, reportimgurl, reportcontent, reporttitle)
            VALUES (report_seq.NEXTVAL, :reporterid,
                    (SELECT userno FROM user_table_real WHERE nickname = :targetNickname),
                    SYSDATE, :imgurl, :content, :title)
            """, nativeQuery = true)
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    void saveReport(@Param("reporterid") int reporterid,
                    @Param("targetNickname") String targetNickname,
                    @Param("imgurl") String imgurl,
                    @Param("content") String content,
                    @Param("title") String title);
}

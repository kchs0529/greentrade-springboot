package com.mega.greentrade.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LogRepository extends JpaRepository<TradeLog, Integer> {

    // 판매 내역
    @Query(value = """
            SELECT l.tradestatus, p.title, p.price, l.tradestartdate, p.paymethod,
                   p.trademethod, p.productno, l.buyuserno
            FROM product p JOIN logs l ON p.productno = l.productno
            WHERE l.selluserno = :userno AND l.tradetype = '판매'
            ORDER BY l.tradestartdate DESC
            """, nativeQuery = true)
    List<SellLogProjection> findSellLogList(@Param("userno") int userno);

    // 구매 내역
    @Query(value = """
            SELECT l.tradestatus, p.title, p.price, l.tradesuccessdate, p.paymethod,
                   p.trademethod, l.buyuserno, p.productno
            FROM product p JOIN logs l ON p.productno = l.productno
            WHERE l.buyuserno = :userno AND l.tradetype = '구매' AND p.sellstatus = '판매완료'
            ORDER BY l.tradestartdate DESC
            """, nativeQuery = true)
    List<BuyLogProjection> findBuyLogList(@Param("userno") int userno);

    @Modifying
    @Transactional
    void deleteByProductno(int productno);

    @Modifying
    @Transactional
    void deleteByProductnoAndSelluserno(int productno, int selluserno);
}

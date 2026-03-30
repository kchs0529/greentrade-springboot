package com.mega.greentrade.repository;
import com.mega.greentrade.dto.BuyListProjection;
import com.mega.greentrade.entity.BuyList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BuyListRepository extends JpaRepository<BuyList, Integer> {

    @Query(value = """
            SELECT bl.buylistdate, bl.isreview, p.image, p.productno, p.title, p.price, p.paymethod,
                   (SELECT user_name FROM user_table_real WHERE userno = bl.selluserno) AS sellername
            FROM buylist bl
            JOIN product p ON bl.productno = p.productno
            WHERE bl.buyuserno = :userno
            ORDER BY bl.buylistdate DESC
            """, nativeQuery = true)
    List<BuyListProjection> findBuyList(@Param("userno") int userno);

    @Modifying
    @Transactional
    @Query("UPDATE BuyList b SET b.isreview = 'yes' WHERE b.productno = :productno AND b.buyuserno = :buyuserno")
    int markAsReviewed(@Param("productno") int productno, @Param("buyuserno") int buyuserno);

    int countByBuyuserno(int buyuserno);
}

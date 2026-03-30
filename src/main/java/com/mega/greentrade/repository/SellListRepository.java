package com.mega.greentrade.repository;
import com.mega.greentrade.dto.SellListProjection;
import com.mega.greentrade.entity.SellList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SellListRepository extends JpaRepository<SellList, Integer> {

    @Query(value = """
            SELECT sl.selllistdate, p.image, p.productno, p.title, p.price, p.paymethod,
                   (SELECT user_name FROM user_table_real WHERE userno = sl.buyuserno) AS buyername
            FROM selllist sl
            JOIN product p ON sl.productno = p.productno
            WHERE sl.selluserno = :userno
            ORDER BY sl.selllistdate DESC
            """, nativeQuery = true)
    List<SellListProjection> findSellList(@Param("userno") int userno);

    int countBySelluserno(int selluserno);
}

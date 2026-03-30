package com.mega.greentrade.heart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Integer> {

    Optional<Heart> findByUsernoAndProductno(int userno, int productno);

    @Modifying
    @Transactional
    void deleteByProductnoAndUserno(int productno, int userno);

    @Modifying
    @Transactional
    void deleteByProductno(int productno);

    @Modifying
    @Transactional
    void deleteByUserno(int userno);

    // 찜 목록 (상품+판매자 정보 포함)
    @Query(value = """
            SELECT h.likeno, h.likedate, h.userno, h.likestat,
                   p.productno, p.title, p.price, p.image, u.user_name, p.productstatus
            FROM product p
            JOIN heart h ON p.productno = h.productno
            JOIN user_table_real u ON p.userno = u.userno
            WHERE h.userno = :userno
            ORDER BY h.likedate DESC
            """, nativeQuery = true)
    List<LikeListProjection> findLikeList(@Param("userno") int userno);
}

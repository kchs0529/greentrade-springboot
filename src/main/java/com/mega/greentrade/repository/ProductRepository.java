package com.mega.greentrade.repository;
import com.mega.greentrade.dto.ProductSellerProjection;
import com.mega.greentrade.dto.ProductWithUserProjection;
import com.mega.greentrade.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // 최신 8개 (메인페이지)
    @Query(value = """
            SELECT p.productno, p.title, p.price, p.image, u.user_name, p.productstatus,
                   p.trademethod, p.adddate, p.paymethod, p.sellstatus, p.userno,
                   p.productdetail, p.tradelocation, p.viewcount, p.likenum, p.editdate
            FROM product p JOIN user_table_real u ON p.userno = u.userno
            WHERE p.sellstatus = '판매중'
            ORDER BY p.adddate DESC
            FETCH FIRST 8 ROWS ONLY
            """, nativeQuery = true)
    List<ProductWithUserProjection> findRecentItems();

    // 전체 판매중 상품 목록 (Pageable)
    @Query(value = """
            SELECT p.productno, p.title, p.price, p.image, u.user_name, p.productstatus,
                   p.trademethod, p.adddate, p.paymethod, p.sellstatus, p.userno,
                   p.productdetail, p.tradelocation, p.viewcount, p.likenum, p.editdate
            FROM product p JOIN user_table_real u ON p.userno = u.userno
            WHERE p.sellstatus = '판매중'
            ORDER BY p.adddate DESC
            """,
           countQuery = "SELECT COUNT(*) FROM product WHERE sellstatus = '판매중'",
           nativeQuery = true)
    Page<ProductWithUserProjection> findProductList(Pageable pageable);

    // 나눔 목록
    @Query(value = """
            SELECT p.productno, p.title, p.price, p.image, u.user_name, p.productstatus,
                   p.trademethod, p.adddate, p.paymethod, p.sellstatus, p.userno,
                   p.productdetail, p.tradelocation, p.viewcount, p.likenum, p.editdate
            FROM product p JOIN user_table_real u ON p.userno = u.userno
            WHERE p.sellstatus = '판매중' AND p.paymethod = '나눔'
            ORDER BY p.adddate DESC
            FETCH FIRST 16 ROWS ONLY
            """, nativeQuery = true)
    List<ProductWithUserProjection> findShareList();

    // 인기 상품 (조회수 순)
    @Query(value = """
            SELECT p.productno, p.title, p.price, p.image, u.user_name, p.productstatus,
                   p.trademethod, p.adddate, p.paymethod, p.sellstatus, p.userno,
                   p.productdetail, p.tradelocation, p.viewcount, p.likenum, p.editdate
            FROM product p JOIN user_table_real u ON p.userno = u.userno
            WHERE p.sellstatus = '판매중'
            ORDER BY p.viewcount DESC
            FETCH FIRST 16 ROWS ONLY
            """, nativeQuery = true)
    List<ProductWithUserProjection> findBestList();

    // 제목 검색
    @Query(value = """
            SELECT p.productno, p.title, p.price, p.image, u.user_name, p.productstatus,
                   p.trademethod, p.adddate, p.paymethod, p.sellstatus, p.userno,
                   p.productdetail, p.tradelocation, p.viewcount, p.likenum, p.editdate
            FROM product p JOIN user_table_real u ON p.userno = u.userno
            WHERE p.title LIKE :keyword
            ORDER BY p.adddate DESC
            """,
           countQuery = "SELECT COUNT(*) FROM product WHERE title LIKE :keyword",
           nativeQuery = true)
    Page<ProductWithUserProjection> searchByTitle(@Param("keyword") String keyword, Pageable pageable);

    // 판매자 상품 목록
    List<Product> findByUsernoOrderByAdddateDesc(int userno);

    // 조회수 증가
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.viewcount = p.viewcount + 1 WHERE p.productno = :productno")
    void increaseViewCount(@Param("productno") int productno);

    // 판매자 정보 (상품에서)
    @Query(value = """
            SELECT u.nickname, u.greenscore, u.userno, u.imgurl
            FROM product p JOIN user_table_real u ON p.userno = u.userno
            WHERE p.productno = :productno
            """, nativeQuery = true)
    ProductSellerProjection findSellerByProductno(@Param("productno") int productno);

    // 관리자 상품 목록 (전체)
    Page<Product> findAll(Pageable pageable);

    // 판매완료 처리
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.sellstatus = '판매완료' WHERE p.productno = :productno")
    void markSold(@Param("productno") int productno);
}

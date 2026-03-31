package com.mega.greentrade.repository;
import com.mega.greentrade.dto.MemberListProjection;
import com.mega.greentrade.dto.SellerInfoProjection;
import com.mega.greentrade.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.user_id = :userId")
    Optional<User> findByUser_id(@Param("userId") String userId);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.user_call = :userCall")
    Optional<User> findByEmailAndUser_call(@Param("email") String email, @Param("userCall") String userCall);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.user_id = :userId")
    boolean existsByUser_id(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.user_password = :password WHERE u.user_id = :userId")
    int updatePassword(@Param("userId") String userId, @Param("password") String password);

    // 판매자 정보 (리뷰 수 포함)
    @Query(value = """
            SELECT u.userno, u.nickname, u.imgurl, u.greenscore,
                   COUNT(r.reviewno) AS reviewCount
            FROM user_table_real u
            LEFT JOIN review r ON r.selleruserno = u.userno
            WHERE u.userno = :userno
            GROUP BY u.userno, u.nickname, u.imgurl, u.greenscore
            """, nativeQuery = true)
    Optional<SellerInfoProjection> findSellerInfo(@Param("userno") int userno);

    // 관리자 회원 목록 (신고 횟수 포함)
    @Query(value = """
            SELECT u.userno, u.user_name, u.user_id,
                   COUNT(r.targetid) AS targetid_count
            FROM user_table_real u
            LEFT JOIN report r ON u.userno = r.targetid
            WHERE u.staff = 'user'
            GROUP BY u.userno, u.user_name, u.user_id
            ORDER BY u.userno DESC
            """,
           countQuery = "SELECT COUNT(*) FROM user_table_real WHERE staff = 'user'",
           nativeQuery = true)
    Page<MemberListProjection> findAllMembers(Pageable pageable);
}

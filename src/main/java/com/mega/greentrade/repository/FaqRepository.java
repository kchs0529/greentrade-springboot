package com.mega.greentrade.repository;
import com.mega.greentrade.entity.Faq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Integer> {
    Page<Faq> findAllByOrderByFaqnoDesc(Pageable pageable);
}

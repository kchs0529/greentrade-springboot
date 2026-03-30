package com.mega.greentrade.faq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Integer> {
    Page<Faq> findAllByOrderByFaqnoDesc(Pageable pageable);
}

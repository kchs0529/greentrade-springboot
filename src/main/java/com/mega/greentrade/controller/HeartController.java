package com.mega.greentrade.controller;
import com.mega.greentrade.entity.Heart;
import com.mega.greentrade.repository.HeartRepository;

import com.mega.greentrade.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

@RestController
@RequestMapping("/heart")
@RequiredArgsConstructor
public class HeartController {

    private final HeartRepository heartRepository;

    @PostMapping("/do")
    @Transactional
    public String doHeart(@RequestParam int productno,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        int userno = userDetails.getUserno();
        boolean exists = heartRepository.findByUsernoAndProductno(userno, productno).isPresent();
        if (!exists) {
            Heart heart = new Heart();
            heart.setLikedate(Date.valueOf(LocalDate.now()));
            heart.setLikestat(1);
            heart.setUserno(userno);
            heart.setProductno(productno);
            heartRepository.save(heart);
        }
        return "ok";
    }

    @PostMapping("/cancel")
    @Transactional
    public String cancelHeart(@RequestParam int productno,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        heartRepository.deleteByProductnoAndUserno(productno, userDetails.getUserno());
        return "ok";
    }
}

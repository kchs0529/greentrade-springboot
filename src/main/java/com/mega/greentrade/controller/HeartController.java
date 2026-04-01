package com.mega.greentrade.controller;
import com.mega.greentrade.entity.Heart;
import com.mega.greentrade.repository.HeartRepository;

import com.mega.greentrade.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

@Tag(name = "Heart", description = "좋아요 관련 API")
@RestController
@RequestMapping("/heart")
@RequiredArgsConstructor
public class HeartController {

    private final HeartRepository heartRepository;

    @Operation(summary = "좋아요 추가")
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

    @Operation(summary = "좋아요 취소")
    @PostMapping("/cancel")
    @Transactional
    public String cancelHeart(@RequestParam int productno,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        heartRepository.deleteByProductnoAndUserno(productno, userDetails.getUserno());
        return "ok";
    }
}

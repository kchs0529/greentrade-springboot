package com.mega.greentrade.heart;

import com.mega.greentrade.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/heart")
@RequiredArgsConstructor
public class HeartController {

    private final HeartDAO heartDAO;

    @PostMapping("/do")
    public String doHeart(@RequestParam int productno,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        heartDAO.doHeart(userDetails.getUserno(), productno);
        return "ok";
    }

    @PostMapping("/cancel")
    public String cancelHeart(@RequestParam int productno,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        heartDAO.cancleHeart(userDetails.getUserno(), productno);
        return "ok";
    }
}

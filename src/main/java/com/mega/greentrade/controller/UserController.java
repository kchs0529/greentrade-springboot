package com.mega.greentrade.controller;
import com.mega.greentrade.dto.JoinDTO;
import com.mega.greentrade.dto.UserDTO;
import com.mega.greentrade.security.CustomUserDetails;
import com.mega.greentrade.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "회원 관련 API")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "로그인 페이지")
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        return "login/login";
    }

    @Operation(summary = "회원가입 페이지")
    @GetMapping("/join")
    public String joinPage() {
        return "login/join";
    }

    @Operation(summary = "회원가입 처리")
    @PostMapping("/join")
    public String join(@ModelAttribute JoinDTO joinDTO, Model model) {
        if (userService.isDuplicateId(joinDTO.getUser_id())) {
            model.addAttribute("errorMsg", "이미 사용 중인 아이디입니다.");
            return "login/join";
        }
        userService.join(joinDTO);
        return "redirect:/login";
    }

    @Operation(summary = "아이디 중복 확인")
    @GetMapping("/join/check-id")
    @ResponseBody
    public String checkDuplicateId(@RequestParam String userId) {
        return userService.isDuplicateId(userId) ? "duplicate" : "available";
    }

    @Operation(summary = "아이디 찾기 페이지")
    @GetMapping("/find-id")
    public String findIdPage() {
        return "login/find_id";
    }

    @Operation(summary = "아이디 찾기 처리")
    @PostMapping("/find-id")
    public String findId(@RequestParam String email,
                         @RequestParam String user_call,
                         Model model) {
        UserDTO user = userService.findUserByEmailAndCall(email, user_call);
        if (user != null) {
            model.addAttribute("foundId", user.getUser_id());
        } else {
            model.addAttribute("errorMsg", "일치하는 회원 정보를 찾을 수 없습니다.");
        }
        return "login/find_id";
    }

    @Operation(summary = "비밀번호 찾기 페이지")
    @GetMapping("/find-password")
    public String findPasswordPage() {
        return "login/find_password";
    }

    @Operation(summary = "임시 비밀번호 이메일 발송")
    @PostMapping("/find-password")
    public String findPassword(@RequestParam String user_id, Model model) {
        boolean sent = userService.sendTempPassword(user_id);
        if (sent) {
            model.addAttribute("successMsg", "임시 비밀번호가 이메일로 전송되었습니다.");
        } else {
            model.addAttribute("errorMsg", "존재하지 않는 아이디입니다.");
        }
        return "login/find_password";
    }

    @Operation(summary = "비밀번호 변경 페이지")
    @GetMapping("/password-update")
    public String updatePasswordPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return "login/update_password";
    }

    @Operation(summary = "비밀번호 변경 처리")
    @PostMapping("/password-update")
    public String updatePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestParam String newPassword) {
        userService.updatePassword(userDetails.getUsername(), newPassword);
        return "redirect:/mypage";
    }
}

package com.mega.greentrade.controller;
import com.mega.greentrade.dto.AddressDTO;
import com.mega.greentrade.repository.AddressRepository;

import com.mega.greentrade.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Address", description = "배송지 관련 API")
@Controller
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressRepository addressRepository;

    @Operation(summary = "배송지 수정 페이지")
    @GetMapping("/update")
    public String updateAddressPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        addressRepository.findByUserid(userDetails.getUsername())
                .ifPresent(addr -> model.addAttribute("address", addr));
        return "mypage/update_address";
    }

    @Operation(summary = "배송지 수정 처리")
    @PostMapping("/update")
    public String updateAddress(@ModelAttribute AddressDTO dto,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUsername();
        addressRepository.findByUserid(userId).ifPresent(addr -> {
            addr.setReceivername(dto.getReceivername());
            addr.setPhone(dto.getPhone());
            addr.setAddress1(dto.getAddress1());
            addr.setAddress2(dto.getAddress2());
            addr.setPostnum(dto.getPostnum());
            addressRepository.save(addr);
        });
        return "redirect:/mypage";
    }
}

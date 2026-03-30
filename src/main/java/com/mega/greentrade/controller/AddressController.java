package com.mega.greentrade.controller;
import com.mega.greentrade.dto.AddressDTO;
import com.mega.greentrade.repository.AddressRepository;

import com.mega.greentrade.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressRepository addressRepository;

    @GetMapping("/update")
    public String updateAddressPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        addressRepository.findByUserid(userDetails.getUsername())
                .ifPresent(addr -> model.addAttribute("address", addr));
        return "mypage/update_address";
    }

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

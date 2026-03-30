package com.mega.greentrade.address;

import com.mega.greentrade.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressDAO addressDAO;

    @GetMapping("/update")
    public String updateAddressPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        AddressDTO address = addressDAO.getAddressByUserId(userDetails.getUsername());
        model.addAttribute("address", address);
        return "mypage/update_address";
    }

    @PostMapping("/update")
    public String updateAddress(@ModelAttribute AddressDTO dto,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        dto.setUserid(userDetails.getUsername());
        addressDAO.updateAddress(dto);
        return "redirect:/mypage";
    }
}

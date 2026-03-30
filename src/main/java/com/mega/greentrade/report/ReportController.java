package com.mega.greentrade.report;

import com.mega.greentrade.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportDAO reportDAO;
    private static final int PAGE_SIZE = 10;

    @GetMapping("/form")
    public String reportForm(@RequestParam int targetUserno, Model model) {
        model.addAttribute("targetUserno", targetUserno);
        return "report/report_form";
    }

    @PostMapping("/submit")
    public String submitReport(@ModelAttribute ReportDTO dto,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        reportDAO.saveReport(dto, userDetails.getUserno());
        return "redirect:/";
    }
}

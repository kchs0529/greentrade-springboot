package com.mega.greentrade.controller;
import com.mega.greentrade.dto.ReportDTO;
import com.mega.greentrade.repository.ReportRepository;

import com.mega.greentrade.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Report", description = "신고 관련 API")
@Controller
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportRepository reportRepository;

    @Operation(summary = "신고 폼 페이지")
    @GetMapping("/form")
    public String reportForm(@RequestParam int targetUserno, Model model) {
        model.addAttribute("targetUserno", targetUserno);
        return "report/report_form";
    }

    @Operation(summary = "신고 제출")
    @PostMapping("/submit")
    public String submitReport(@ModelAttribute ReportDTO dto,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        reportRepository.saveReport(
                userDetails.getUserno(),
                dto.getTargetNickname(),
                dto.getReportimgurl(),
                dto.getReportcontent(),
                dto.getReporttitle()
        );
        return "redirect:/";
    }
}

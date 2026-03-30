package com.mega.greentrade.faq;

import com.mega.greentrade.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqDAO faqDAO;
    private static final int PAGE_SIZE = 10;

    @GetMapping
    public String faqList(@RequestParam(defaultValue = "1") int page, Model model) {
        int startRow = (page - 1) * PAGE_SIZE + 1;
        int endRow = page * PAGE_SIZE;
        List<FaqDTO> list = faqDAO.getFaqList(startRow, endRow);
        int totalCount = faqDAO.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        model.addAttribute("faqList", list);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "faq/faq_list";
    }

    @GetMapping("/{faqno}")
    public String faqContent(@PathVariable int faqno, Model model) {
        model.addAttribute("faq", faqDAO.getFaqContent(faqno));
        return "faq/faq_content";
    }

    @GetMapping("/write")
    public String faqWritePage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return "faq/faq_form";
    }

    @PostMapping("/write")
    public String saveFaq(@ModelAttribute FaqDTO dto) {
        faqDAO.saveFaq(dto);
        return "redirect:/faq";
    }
}

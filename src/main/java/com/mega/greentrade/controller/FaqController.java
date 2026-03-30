package com.mega.greentrade.controller;
import com.mega.greentrade.dto.FaqDTO;
import com.mega.greentrade.entity.Faq;
import com.mega.greentrade.repository.FaqRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

@Controller
@RequestMapping("/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqRepository faqRepository;
    private static final int PAGE_SIZE = 10;

    @GetMapping
    public String faqList(@RequestParam(defaultValue = "1") int page, Model model) {
        Page<Faq> faqPage = faqRepository.findAllByOrderByFaqnoDesc(PageRequest.of(page - 1, PAGE_SIZE));
        model.addAttribute("faqList", faqPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", faqPage.getTotalPages());
        return "faq/faq_list";
    }

    @GetMapping("/{faqno}")
    public String faqContent(@PathVariable int faqno, Model model) {
        faqRepository.findById(faqno).ifPresent(faq -> model.addAttribute("faq", faq));
        return "faq/faq_content";
    }

    @GetMapping("/write")
    public String faqWritePage() {
        return "faq/faq_form";
    }

    @PostMapping("/write")
    public String saveFaq(@ModelAttribute FaqDTO dto) {
        Faq faq = new Faq();
        faq.setFaqtitle(dto.getFaqtitle());
        faq.setFaqcontent(dto.getFaqcontent());
        faq.setFaqcate(dto.getFaqcate());
        faq.setFaqdate(Date.valueOf(LocalDate.now()));
        faqRepository.save(faq);
        return "redirect:/faq";
    }
}

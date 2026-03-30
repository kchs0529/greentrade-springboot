package com.mega.greentrade.controller;

import com.mega.greentrade.service.ProductService;
import com.mega.greentrade.dto.ReportDetailProjection;
import com.mega.greentrade.repository.ReportRepository;
import com.mega.greentrade.dto.MemberListProjection;
import com.mega.greentrade.repository.UserRepository;
import com.mega.greentrade.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ProductService productService;
    private final ReportRepository reportRepository;
    private static final int PAGE_SIZE = 10;

    @GetMapping
    public String managerMain() {
        return "manager/manager_main";
    }

    @GetMapping("/members")
    public String memberList(@RequestParam(defaultValue = "1") int page, Model model) {
        Page<MemberListProjection> memberPage = userRepository.findAllMembers(PageRequest.of(page - 1, PAGE_SIZE));
        model.addAttribute("members", memberPage.getContent());
        model.addAttribute("totalPages", memberPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "manager/manager_memberList";
    }

    @GetMapping("/members/{userId}")
    public String memberInfo(@PathVariable String userId, Model model) {
        userRepository.findByUser_id(userId).ifPresent(user -> model.addAttribute("member", user));
        return "manager/manager_memberInfo";
    }

    @PostMapping("/members/delete")
    public String deleteMember(@RequestParam String userId, @RequestParam int userno) {
        userService.deleteUser(userno);
        return "redirect:/manager/members";
    }

    @GetMapping("/products")
    public String productList(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 12;
        var productPage = productService.getAllProducts(page, pageSize);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "manager/manager_product";
    }

    @PostMapping("/products/delete")
    public String deleteProduct(@RequestParam int productno) {
        productService.deleteProduct(productno);
        return "redirect:/manager/products";
    }

    @GetMapping("/reports")
    public String reportList(@RequestParam(defaultValue = "1") int page, Model model) {
        Page<ReportDetailProjection> reportPage = reportRepository.findAllReports(PageRequest.of(page - 1, PAGE_SIZE));
        model.addAttribute("reports", reportPage.getContent());
        model.addAttribute("totalPages", reportPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "manager/manager_reportList";
    }

    @GetMapping("/reports/{reportid}")
    public String reportContent(@PathVariable int reportid, Model model) {
        model.addAttribute("report", reportRepository.findReportDetail(reportid));
        return "manager/manager_reportContent";
    }

    @GetMapping("/reports/user/{userno}")
    public String reportsByUser(@PathVariable int userno,
                                @RequestParam(defaultValue = "1") int page,
                                Model model) {
        Page<ReportDetailProjection> reportPage = reportRepository.findReportsByTarget(userno, PageRequest.of(page - 1, PAGE_SIZE));
        model.addAttribute("reports", reportPage.getContent());
        model.addAttribute("totalPages", reportPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("targetUserno", userno);
        return "manager/manager_reportList";
    }
}

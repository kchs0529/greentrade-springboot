package com.mega.greentrade.manager;

import com.mega.greentrade.product.ProductDAO;
import com.mega.greentrade.report.ReportDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerDAO managerDAO;
    private final ProductDAO productDAO;
    private final ReportDAO reportDAO;
    private static final int PAGE_SIZE = 10;

    @GetMapping
    public String managerMain() {
        return "manager/manager_main";
    }

    @GetMapping("/members")
    public String memberList(@RequestParam(defaultValue = "1") int page, Model model) {
        int startRow = (page - 1) * PAGE_SIZE + 1;
        int endRow = page * PAGE_SIZE;
        model.addAttribute("members", managerDAO.getMemberList(startRow, endRow));
        model.addAttribute("totalPages", (int) Math.ceil((double) managerDAO.getTotalMemberCount() / PAGE_SIZE));
        model.addAttribute("currentPage", page);
        return "manager/manager_memberList";
    }

    @GetMapping("/members/{userId}")
    public String memberInfo(@PathVariable String userId, Model model) {
        model.addAttribute("member", managerDAO.getMemberInfo(userId));
        return "manager/manager_memberInfo";
    }

    @PostMapping("/members/delete")
    public String deleteMember(@RequestParam String userId, @RequestParam int userno) {
        managerDAO.deleteMember(userId, userno);
        return "redirect:/manager/members";
    }

    @GetMapping("/products")
    public String productList(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 12;
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;
        model.addAttribute("products", productDAO.getAllProducts(startRow, endRow));
        model.addAttribute("totalPages", (int) Math.ceil((double) productDAO.getTotalCount() / pageSize));
        model.addAttribute("currentPage", page);
        return "manager/manager_product";
    }

    @PostMapping("/products/delete")
    public String deleteProduct(@RequestParam int productno) {
        productDAO.managerDeleteProduct(productno);
        return "redirect:/manager/products";
    }

    @GetMapping("/reports")
    public String reportList(@RequestParam(defaultValue = "1") int page, Model model) {
        int startRow = (page - 1) * PAGE_SIZE + 1;
        int endRow = page * PAGE_SIZE;
        model.addAttribute("reports", reportDAO.getReportList(startRow, endRow));
        model.addAttribute("totalPages", (int) Math.ceil((double) reportDAO.getTotalCount() / PAGE_SIZE));
        model.addAttribute("currentPage", page);
        return "manager/manager_reportList";
    }

    @GetMapping("/reports/{reportid}")
    public String reportContent(@PathVariable int reportid, Model model) {
        model.addAttribute("report", reportDAO.getReportContent(reportid));
        return "manager/manager_reportContent";
    }

    @GetMapping("/reports/user/{userno}")
    public String reportsByUser(@PathVariable int userno,
                                @RequestParam(defaultValue = "1") int page,
                                Model model) {
        int startRow = (page - 1) * PAGE_SIZE + 1;
        int endRow = page * PAGE_SIZE;
        model.addAttribute("reports", reportDAO.getReportListByTarget(userno, startRow, endRow));
        model.addAttribute("totalPages", (int) Math.ceil((double) reportDAO.getTotalCountByTarget(userno) / PAGE_SIZE));
        model.addAttribute("currentPage", page);
        model.addAttribute("targetUserno", userno);
        return "manager/manager_reportList";
    }
}

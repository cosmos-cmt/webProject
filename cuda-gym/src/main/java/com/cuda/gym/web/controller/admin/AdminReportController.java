package com.cuda.gym.web.controller.admin;

import com.cuda.gym.domain.service.ReportService;
import com.cuda.gym.web.viewmodel.admin.ClassPopularityReportRow;
import com.cuda.gym.web.viewmodel.admin.MembershipReportRow;
import com.cuda.gym.web.viewmodel.admin.RevenueReportRow;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/reports")
public class AdminReportController {

    private final ReportService reportService;

    public AdminReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    
    @GetMapping
    public String showReportsOverview(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

                if (from == null || to == null) {
            LocalDate now = LocalDate.now();
            to = now;
            from = now.minusMonths(12);
        }

        List<RevenueReportRow> revenueReport =
                reportService.getRevenueReport(from, to);
        List<MembershipReportRow> membershipReport =
                reportService.getMembershipReport();
        List<ClassPopularityReportRow> classPopularityReport =
                reportService.getClassPopularityReport(from, to);

        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("revenueReport", revenueReport);
        model.addAttribute("membershipReport", membershipReport);
        model.addAttribute("classPopularityReport", classPopularityReport);

        return "admin/reports/overview";
    }

   
    @GetMapping("/members/export")
    public void exportMembersToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"members.csv\"");

        try (PrintWriter writer = response.getWriter()) {
                        writer.println("ID,Full Name,Email,Phone,Date of Birth,Role,Created At,Status,Plan");

            reportService.streamAllMembers(member -> {
                String csvLine = String.join(",",
                        safe(member.getId()),
                        safe(member.getFullName()),
                        safe(member.getEmail()),
                        safe(member.getPhone()),
                        safeDate(member.getDateOfBirth()),
                        safe(member.getRole() != null ? member.getRole().name() : ""),
                        safeDateTime(member.getCreatedAt()),
                        safe(member.getStatus()),
                        safe(member.getCurrentPlanName()));
                writer.println(csvLine);
            });
        }
    }

    
    @GetMapping("/subscriptions/summary")
    public String subscriptionSummaryPrintable(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        if (from == null || to == null) {
            LocalDate now = LocalDate.now();
            to = now;
            from = now.minusMonths(12);
        }

        List<RevenueReportRow> revenueReport =
                reportService.getRevenueReport(from, to);
        List<MembershipReportRow> membershipReport =
                reportService.getMembershipReport();

        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("revenueReport", revenueReport);
        model.addAttribute("membershipReport", membershipReport);

               return "admin/reports/subscription-summary-print";
    }

   
    private String safe(Object value) {
        if (value == null) return "";
        String s = String.valueOf(value);
        // Wrap in quotes and escape quotes for simple CSV safety
        s = s.replace("\"", "\"\"");
        return "\"" + s + "\"";
    }

    private String safeDate(LocalDate date) {
        return date == null ? "" : "\"" + date.toString() + "\"";
    }

    private String safeDateTime(Object dateTime) {
        return dateTime == null ? "" : "\"" + dateTime.toString() + "\"";
    }
}
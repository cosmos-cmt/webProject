package com.cuda.gym.web.controller.admin;

import com.cuda.gym.domain.service.DashboardService;
import com.cuda.gym.web.viewmodel.admin.DashboardStatsViewModel;
import com.cuda.gym.web.viewmodel.admin.RecentRegistrationViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminDashboardController {

    private final DashboardService dashboardService;

    public AdminDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model) {

              DashboardStatsViewModel stats = new DashboardStatsViewModel();
        stats.setTotalMembers(dashboardService.countTotalMembers());
        stats.setActiveSubscriptions(dashboardService.countActiveSubscriptions());
        stats.setClassesPerWeek(dashboardService.countClassesPerWeek());
        stats.setRevenueThisMonth(dashboardService.calculateRevenueThisMonth());
        stats.setMostPopularPlanName(dashboardService.findMostPopularPlanName());

        model.addAttribute("stats", stats);

                List<RecentRegistrationViewModel> recentRegistrations =
                dashboardService.findRecentRegistrations(5);
        model.addAttribute("recentRegistrations", recentRegistrations);

        
        model.addAttribute("membersByPlan", dashboardService.getMembersCountByPlan());

        return "admin/dashboard";    }
}
package com.cuda.gym.web.viewmodel;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdminDashboardViewModel {

           private long totalMembers;

        private long activeSubscriptions;

       private long classesToday;

        private double revenueThisMonth;

    
        private Map<String, String> recentRegistrations = new LinkedHashMap<>();

   
       private Map<String, Long> membersPerPlan = new LinkedHashMap<>();

       private Map<LocalDate, Long> newMembersPerDay = new LinkedHashMap<>();

        private Map<LocalDate, Double> revenuePerDay = new LinkedHashMap<>();

    public AdminDashboardViewModel() {
    }

    public long getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(long totalMembers) {
        this.totalMembers = totalMembers;
    }

    public long getActiveSubscriptions() {
        return activeSubscriptions;
    }

    public void setActiveSubscriptions(long activeSubscriptions) {
        this.activeSubscriptions = activeSubscriptions;
    }

    public long getClassesToday() {
        return classesToday;
    }

    public void setClassesToday(long classesToday) {
        this.classesToday = classesToday;
    }

    public double getRevenueThisMonth() {
        return revenueThisMonth;
    }

    public void setRevenueThisMonth(double revenueThisMonth) {
        this.revenueThisMonth = revenueThisMonth;
    }

    public Map<String, String> getRecentRegistrations() {
        return recentRegistrations;
    }

    public void setRecentRegistrations(Map<String, String> recentRegistrations) {
        this.recentRegistrations =
                recentRegistrations != null ? recentRegistrations : new LinkedHashMap<>();
    }

    public Map<String, Long> getMembersPerPlan() {
        return membersPerPlan;
    }

    public void setMembersPerPlan(Map<String, Long> membersPerPlan) {
        this.membersPerPlan =
                membersPerPlan != null ? membersPerPlan : new LinkedHashMap<>();
    }

    public Map<LocalDate, Long> getNewMembersPerDay() {
        return newMembersPerDay;
    }

    public void setNewMembersPerDay(Map<LocalDate, Long> newMembersPerDay) {
        this.newMembersPerDay =
                newMembersPerDay != null ? newMembersPerDay : new LinkedHashMap<>();
    }

    public Map<LocalDate, Double> getRevenuePerDay() {
        return revenuePerDay;
    }

    public void setRevenuePerDay(Map<LocalDate, Double> revenuePerDay) {
        this.revenuePerDay =
                revenuePerDay != null ? revenuePerDay : new LinkedHashMap<>();
    }
}

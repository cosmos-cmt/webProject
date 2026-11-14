package com.cuda.gym.web.viewmodel;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportViewModel {

            private LocalDate fromDate;

       private LocalDate toDate;

        private YearMonth selectedMonth;

   
        private Map<String, Long> membersPerPlan = new LinkedHashMap<>();

  
        private Map<String, Long> bookingsPerClass = new LinkedHashMap<>();

  
    private Map<LocalDate, Long> bookingsPerDay = new LinkedHashMap<>();

        private Map<YearMonth, Double> revenuePerMonth = new LinkedHashMap<>();

    public ReportViewModel() {
    }

  
    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public YearMonth getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(YearMonth selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public Map<String, Long> getMembersPerPlan() {
        return membersPerPlan;
    }

    public void setMembersPerPlan(Map<String, Long> membersPerPlan) {
        this.membersPerPlan =
                membersPerPlan != null ? membersPerPlan : new LinkedHashMap<>();
    }

    public Map<String, Long> getBookingsPerClass() {
        return bookingsPerClass;
    }

    public void setBookingsPerClass(Map<String, Long> bookingsPerClass) {
        this.bookingsPerClass =
                bookingsPerClass != null ? bookingsPerClass : new LinkedHashMap<>();
    }

    public Map<LocalDate, Long> getBookingsPerDay() {
        return bookingsPerDay;
    }

    public void setBookingsPerDay(Map<LocalDate, Long> bookingsPerDay) {
        this.bookingsPerDay =
                bookingsPerDay != null ? bookingsPerDay : new LinkedHashMap<>();
    }

    public Map<YearMonth, Double> getRevenuePerMonth() {
        return revenuePerMonth;
    }

    public void setRevenuePerMonth(Map<YearMonth, Double> revenuePerMonth) {
        this.revenuePerMonth =
                revenuePerMonth != null ? revenuePerMonth : new LinkedHashMap<>();
    }
}
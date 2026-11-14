package com.cuda.gym.domain.service.report;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

public interface ReportingService {

    
    
    long getTotalUsers();

        long getTotalTrainers();

       long getTotalClasses();

        long getTotalBookings();

        long getTotalContactMessages();


    long getActiveUsersCount();

        long getNewUsersSince(LocalDate date);

  
       Map<String, Long> getClassCountByDifficulty();

        Map<String, Long> getClassCountByTrainer();

        long getBookingsForClass(Long classId);

        long getBookingsForTrainer(Long trainerId);

        long getBookingsSince(LocalDate date);

 
    Map<LocalDate, Long> getDailyBookingsForMonth(YearMonth month);

         Map<String, Long> getContactMessageCountByStatus();

       long getUnreadContactMessagesCount();

       DashboardSummary getDashboardSummary();

      class DashboardSummary {

        private long totalUsers;
        private long activeUsers;
        private long totalTrainers;
        private long totalClasses;
        private long totalBookings;
        private long bookingsThisMonth;
        private long unreadContactMessages;

        public DashboardSummary() {
        }

        public long getTotalUsers() {
            return totalUsers;
        }

        public void setTotalUsers(long totalUsers) {
            this.totalUsers = totalUsers;
        }

        public long getActiveUsers() {
            return activeUsers;
        }

        public void setActiveUsers(long activeUsers) {
            this.activeUsers = activeUsers;
        }

        public long getTotalTrainers() {
            return totalTrainers;
        }

        public void setTotalTrainers(long totalTrainers) {
            this.totalTrainers = totalTrainers;
        }

        public long getTotalClasses() {
            return totalClasses;
        }

        public void setTotalClasses(long totalClasses) {
            this.totalClasses = totalClasses;
        }

        public long getTotalBookings() {
            return totalBookings;
        }

        public void setTotalBookings(long totalBookings) {
            this.totalBookings = totalBookings;
        }

        public long getBookingsThisMonth() {
            return bookingsThisMonth;
        }

        public void setBookingsThisMonth(long bookingsThisMonth) {
            this.bookingsThisMonth = bookingsThisMonth;
        }

        public long getUnreadContactMessages() {
            return unreadContactMessages;
        }

        public void setUnreadContactMessages(long unreadContactMessages) {
            this.unreadContactMessages = unreadContactMessages;
        }
    }
}
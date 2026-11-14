package com.cuda.gym.domain.service.report;

import com.cuda.gym.domain.repository.ClassBookingRepository;
import com.cuda.gym.domain.repository.ContactMessageRepository;
import com.cuda.gym.domain.repository.GymClassRepository;
import com.cuda.gym.domain.repository.TrainerRepository;
import com.cuda.gym.domain.repository.UserRepository;
import com.cuda.gym.domain.service.report.ReportingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ReportingServiceImpl implements ReportingService {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final GymClassRepository gymClassRepository;
    private final ClassBookingRepository bookingRepository;
    private final ContactMessageRepository contactMessageRepository;

    public ReportingServiceImpl(UserRepository userRepository,
                                TrainerRepository trainerRepository,
                                GymClassRepository gymClassRepository,
                                ClassBookingRepository bookingRepository,
                                ContactMessageRepository contactMessageRepository) {
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
        this.gymClassRepository = gymClassRepository;
        this.bookingRepository = bookingRepository;
        this.contactMessageRepository = contactMessageRepository;
    }


    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getTotalTrainers() {
        return trainerRepository.count();
    }

    @Override
    public long getTotalClasses() {
        return gymClassRepository.count();
    }

    @Override
    public long getTotalBookings() {
        return bookingRepository.count();
    }

    @Override
    public long getTotalContactMessages() {
        return contactMessageRepository.count();
    }

    @Override
    public long getActiveUsersCount() {
        
        return userRepository.countByActiveTrue();
    }

    @Override
    public long getNewUsersSince(LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        return userRepository.countByCreatedAtGreaterThanEqual(from);
    }

    @Override
    public Map<String, Long> getClassCountByDifficulty() {
               List<Object[]> rows = gymClassRepository.findClassCountGroupedByDifficulty();
        Map<String, Long> result = new HashMap<>();

        for (Object[] row : rows) {
            String difficulty = (String) row[0];
            Long count = (Long) row[1];
            result.put(difficulty, count);
        }

        return result;
    }

    @Override
    public Map<String, Long> getClassCountByTrainer() {
               List<Object[]> rows = gymClassRepository.findClassCountGroupedByTrainerName();
        Map<String, Long> result = new HashMap<>();

        for (Object[] row : rows) {
            String trainerName = (String) row[0];
            Long count = (Long) row[1];
            result.put(trainerName, count);
        }

        return result;
    }

    @Override
    public long getBookingsForClass(Long classId) {
        return bookingRepository.countByGymClassId(classId);
    }

    @Override
    public long getBookingsForTrainer(Long trainerId) {
                return bookingRepository.countByGymClassTrainerId(trainerId);
    }

    @Override
    public long getBookingsSince(LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        return bookingRepository.countByCreatedAtGreaterThanEqual(from);
    }

    @Override
    public Map<LocalDate, Long> getDailyBookingsForMonth(YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

                List<Object[]> rows = bookingRepository.findDailyBookingCounts(
                start.atStartOfDay(),
                end.plusDays(1).atStartOfDay()
        );

        Map<LocalDate, Long> result = new HashMap<>();
        for (Object[] row : rows) {
            LocalDate day = (LocalDate) row[0];
            Long count = (Long) row[1];
            result.put(day, count);
        }

        return result;
    }

       @Override
    public Map<String, Long> getContactMessageCountByStatus() {
               List<Object[]> rows = contactMessageRepository.findMessageCountGroupedByStatus();
        Map<String, Long> result = new HashMap<>();

        for (Object[] row : rows) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            result.put(status, count);
        }

        return result;
    }

    @Override
    public long getUnreadContactMessagesCount() {
        return contactMessageRepository.countByReadStatus("NEW");
    }

        @Override
    public ReportingService.DashboardSummary getDashboardSummary() {
        ReportingService.DashboardSummary summary = new ReportingService.DashboardSummary();

        summary.setTotalUsers(getTotalUsers());
        summary.setActiveUsers(getActiveUsersCount());
        summary.setTotalTrainers(getTotalTrainers());
        summary.setTotalClasses(getTotalClasses());
        summary.setTotalBookings(getTotalBookings());
        summary.setUnreadContactMessages(getUnreadContactMessagesCount());

        YearMonth currentMonth = YearMonth.now();
        LocalDate monthStart = currentMonth.atDay(1);
        summary.setBookingsThisMonth(getBookingsSince(monthStart));

        return summary;
    }
}
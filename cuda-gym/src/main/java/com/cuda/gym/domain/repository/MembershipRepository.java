package com.cuda.gym.domain.repository;

import com.cuda.gym.domain.model.Membership;
import com.cuda.gym.domain.model.Plan;
import com.cuda.gym.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByUserOrderByStartDateDesc(User user);

       Page<Membership> findByUserOrderByStartDateDesc(User user, Pageable pageable);

        List<Membership> findByPlan(Plan plan);

        List<Membership> findByStatus(String status);

        Page<Membership> findByStatus(String status, Pageable pageable);

        Optional<Membership> findFirstByUserAndStatus(User user, String status);

        List<Membership> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(
            LocalDate end,
            LocalDate start
    );

       List<Membership> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

        long countByStatus(String status);

    long countByPlanAndStatus(Plan plan, String status);

        long countByPlan(Plan plan);

        boolean existsByUserAndStatus(User user, String status);
}
package com.cuda.gym.domain.repository;

import com.cuda.gym.domain.model.ClassBooking;
import com.cuda.gym.domain.model.GymClass;
import com.cuda.gym.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClassBookingRepository extends JpaRepository<ClassBooking, Long> {

        List<ClassBooking> findByUserOrderByBookedAtDesc(User user);

        Page<ClassBooking> findByUserOrderByBookedAtDesc(User user, Pageable pageable);

        List<ClassBooking> findByGymClass(GymClass gymClass);

       long countByGymClass(GymClass gymClass);

        boolean existsByUserAndGymClass(User user, GymClass gymClass);

        Optional<ClassBooking> findByUserAndGymClass(User user, GymClass gymClass);

        List<ClassBooking> findByBookedAtBetween(LocalDateTime start, LocalDateTime end);

        long countByUser(User user);
}
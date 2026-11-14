package com.cuda.gym.domain.repository;

import com.cuda.gym.domain.model.GymClass;
import com.cuda.gym.domain.model.Trainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface GymClassRepository extends JpaRepository<GymClass, Long> {

       List<GymClass> findByTrainerOrderByDayOfWeekAscStartTimeAsc(Trainer trainer);

      List<GymClass> findByDayOfWeekOrderByStartTimeAsc(DayOfWeek dayOfWeek);

       List<GymClass> findByDifficulty(String difficulty);

        Page<GymClass> findByTrainerAndDifficulty(
            Trainer trainer,
            String difficulty,
            Pageable pageable
    );

        Page<GymClass> findByDifficulty(String difficulty, Pageable pageable);
    Page<GymClass> findByTrainer(Trainer trainer, Pageable pageable);

      List<GymClass> findByTrainerAndDayOfWeek(Trainer trainer, DayOfWeek dayOfWeek);

       long countByDifficulty(String difficulty);

       long countByTrainer(Trainer trainer);
}
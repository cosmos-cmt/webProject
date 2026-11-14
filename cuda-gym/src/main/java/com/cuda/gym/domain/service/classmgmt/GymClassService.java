package com.cuda.gym.domain.service.classmgmt;

import com.cuda.gym.domain.model.GymClass;
import com.cuda.gym.domain.model.Trainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface GymClassService {

        Optional<GymClass> findById(Long id);

       GymClass getByIdOrThrow(Long id);

        List<GymClass> findAll();

       Page<GymClass> findAll(Pageable pageable);

       List<GymClass> findByTrainer(Trainer trainer);

       Page<GymClass> findByTrainer(Trainer trainer, Pageable pageable);

        List<GymClass> findByDayOfWeek(DayOfWeek dayOfWeek);

        List<GymClass> findByDifficulty(String difficulty);

       Page<GymClass> findByTrainerAndDifficulty(Trainer trainer, String difficulty, Pageable pageable);

        Page<GymClass> findByDifficulty(String difficulty, Pageable pageable);

        Page<GymClass> findByTrainerPaged(Trainer trainer, Pageable pageable);

        GymClass createClass(GymClass gymClass);

        GymClass updateClass(Long classId, GymClass updatedClass);

        void deleteClass(Long classId);
    boolean hasTrainerConflict(Trainer trainer, DayOfWeek dayOfWeek, LocalTime startTime);

       long countAllClasses();

        long countByDifficulty(String difficulty);

       long countByTrainer(Trainer trainer);
}

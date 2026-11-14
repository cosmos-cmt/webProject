package com.cuda.gym.domain.repository;

import com.cuda.gym.domain.model.Trainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

        Optional<Trainer> findByEmail(String email);

        boolean existsByEmail(String email);

        List<Trainer> findByNameContainingIgnoreCase(String nameKeyword);

        List<Trainer> findBySpecialtyContainingIgnoreCase(String specialtyKeyword);

        Page<Trainer> findByNameContainingIgnoreCaseOrSpecialtyContainingIgnoreCase(
            String nameKeyword,
            String specialtyKeyword,
            Pageable pageable
    );
    List<Trainer> findAllByOrderByYearsExperienceDesc();

       List<Trainer> findTop3ByOrderByYearsExperienceDesc();
}
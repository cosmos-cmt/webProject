package com.cuda.gym.domain.service.trainer;

import com.cuda.gym.domain.model.Trainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TrainerService {

    Optional<Trainer> findById(Long id);

        Trainer getByIdOrThrow(Long id);

       Optional<Trainer> findByEmail(String email);

        boolean emailExists(String email);

       List<Trainer> findAll();

   
    Page<Trainer> findAll(Pageable pageable);

        List<Trainer> findAllOrderByExperienceDesc();

    
       Trainer createTrainer(Trainer trainer);

        Trainer updateTrainer(Long trainerId, Trainer updatedTrainer);

       void deleteTrainer(Long trainerId);

    
        List<Trainer> searchByName(String keyword);

        List<Trainer> searchBySpecialty(String keyword);

       Page<Trainer> searchByNameOrSpecialty(String keyword, Pageable pageable);

           List<Trainer> findTopExperiencedTrainers(int limit);
    long countAllTrainers();
}
package com.cuda.gym.domain.service.trainer;

import com.cuda.gym.domain.model.Trainer;
import com.cuda.gym.domain.repository.TrainerRepository;
import com.cuda.gym.domain.service.trainer.TrainerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    
    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> findById(Long id) {
        return trainerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer getByIdOrThrow(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return trainerRepository.findByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        if (email == null) {
            return false;
        }
        return trainerRepository.existsByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> findAll() {
        return trainerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Trainer> findAll(Pageable pageable) {
        return trainerRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> findAllOrderByExperienceDesc() {
        return trainerRepository.findAllByOrderByYearsExperienceDesc();
    }

        @Override
    public Trainer createTrainer(Trainer trainer) {
        if (trainer == null) {
            throw new IllegalArgumentException("Trainer must not be null");
        }

        String email = trainer.getEmail();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Trainer email must not be blank");
        }

        String normalizedEmail = email.trim().toLowerCase();
        if (emailExists(normalizedEmail)) {
            throw new IllegalArgumentException("Trainer email already exists: " + normalizedEmail);
        }

        trainer.setEmail(normalizedEmail);

        if (trainer.getName() != null) {
            trainer.setName(trainer.getName().trim());
        }

                if (trainer.getYearsExperience() < 0) {
            trainer.setYearsExperience(0);
        }

        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer updateTrainer(Long trainerId, Trainer updatedTrainer) {
        if (updatedTrainer == null) {
            throw new IllegalArgumentException("Updated trainer must not be null");
        }

        Trainer existing = getByIdOrThrow(trainerId);

                if (updatedTrainer.getEmail() != null && !updatedTrainer.getEmail().isBlank()) {
            String newEmail = updatedTrainer.getEmail().trim().toLowerCase();
            if (!newEmail.equals(existing.getEmail()) && emailExists(newEmail)) {
                throw new IllegalArgumentException("Trainer email already exists: " + newEmail);
            }
            existing.setEmail(newEmail);
        }

                if (updatedTrainer.getName() != null && !updatedTrainer.getName().isBlank()) {
            existing.setName(updatedTrainer.getName().trim());
        }

        
        if (updatedTrainer.getSpecialty() != null && !updatedTrainer.getSpecialty().isBlank()) {
            existing.setSpecialty(updatedTrainer.getSpecialty().trim());
        }

        if (updatedTrainer.getBio() != null && !updatedTrainer.getBio().isBlank()) {
            existing.setBio(updatedTrainer.getBio().trim());
        }

                if (updatedTrainer.getPhotoPath() != null && !updatedTrainer.getPhotoPath().isBlank()) {
            existing.setPhotoPath(updatedTrainer.getPhotoPath().trim());
        }

               if (updatedTrainer.getYearsExperience() >= 0) {
            existing.setYearsExperience(updatedTrainer.getYearsExperience());
        }

        return trainerRepository.save(existing);
    }

    @Override
    public void deleteTrainer(Long trainerId) {
        Trainer existing = getByIdOrThrow(trainerId);
        
        trainerRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAllOrderByExperienceDesc();
        }
        return trainerRepository.findByNameContainingIgnoreCase(keyword.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> searchBySpecialty(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAllOrderByExperienceDesc();
        }
        return trainerRepository.findBySpecialtyContainingIgnoreCase(keyword.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Trainer> searchByNameOrSpecialty(String keyword, Pageable pageable) {
        String search = (keyword == null) ? "" : keyword.trim();
        return trainerRepository
                .findByNameContainingIgnoreCaseOrSpecialtyContainingIgnoreCase(search, search, pageable);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<Trainer> findTopExperiencedTrainers(int limit) {
        if (limit <= 0) {
            return new ArrayList<>();
        }
        List<Trainer> allSorted = trainerRepository.findAllByOrderByYearsExperienceDesc();
        if (allSorted.size() <= limit) {
            return allSorted;
        }
        return allSorted.subList(0, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllTrainers() {
        return trainerRepository.count();
    }
}
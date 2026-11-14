package com.cuda.gym.domain.service.classmgmt;

import com.cuda.gym.domain.model.GymClass;
import com.cuda.gym.domain.model.Trainer;
import com.cuda.gym.domain.repository.GymClassRepository;
import com.cuda.gym.domain.service.classmgmt.GymClassService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class GymClassServiceImpl implements GymClassService {

    private final GymClassRepository gymClassRepository;

    public GymClassServiceImpl(GymClassRepository gymClassRepository) {
        this.gymClassRepository = gymClassRepository;
    }

        @Override
    @Transactional(readOnly = true)
    public Optional<GymClass> findById(Long id) {
        return gymClassRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public GymClass getByIdOrThrow(Long id) {
        return gymClassRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gym class not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymClass> findAll() {
        return gymClassRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GymClass> findAll(Pageable pageable) {
        return gymClassRepository.findAll(pageable);
    }



    @Override
    @Transactional(readOnly = true)
    public List<GymClass> findByTrainer(Trainer trainer) {
        return gymClassRepository.findByTrainerOrderByDayOfWeekAscStartTimeAsc(trainer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GymClass> findByTrainer(Trainer trainer, Pageable pageable) {
        return gymClassRepository.findByTrainer(trainer, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymClass> findByDayOfWeek(DayOfWeek dayOfWeek) {
        return gymClassRepository.findByDayOfWeekOrderByStartTimeAsc(dayOfWeek);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymClass> findByDifficulty(String difficulty) {
        return gymClassRepository.findByDifficulty(difficulty);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GymClass> findByTrainerAndDifficulty(Trainer trainer, String difficulty, Pageable pageable) {
        return gymClassRepository.findByTrainerAndDifficulty(trainer, difficulty, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GymClass> findByDifficulty(String difficulty, Pageable pageable) {
        return gymClassRepository.findByDifficulty(difficulty, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GymClass> findByTrainerPaged(Trainer trainer, Pageable pageable) {
        return gymClassRepository.findByTrainer(trainer, pageable);
    }

    @Override
    public GymClass createClass(GymClass gymClass) {
        if (gymClass == null) {
            throw new IllegalArgumentException("GymClass must not be null");
        }
        if (gymClass.getTrainer() == null || gymClass.getTrainer().getId() == null) {
            throw new IllegalArgumentException("GymClass must have a valid trainer");
        }
        if (gymClass.getDayOfWeek() == null) {
            throw new IllegalArgumentException("GymClass must have a dayOfWeek");
        }
        if (gymClass.getStartTime() == null) {
            throw new IllegalArgumentException("GymClass must have a startTime");
        }
        if (gymClass.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("GymClass duration must be positive");
        }
        if (gymClass.getCapacity() <= 0) {
            throw new IllegalArgumentException("GymClass capacity must be positive");
        }

               if (hasTrainerConflict(gymClass.getTrainer(), gymClass.getDayOfWeek(), gymClass.getStartTime())) {
            throw new IllegalStateException("Trainer already has a class at this day and time");
        }

        return gymClassRepository.save(gymClass);
    }

    @Override
    public GymClass updateClass(Long classId, GymClass updatedClass) {
        if (updatedClass == null) {
            throw new IllegalArgumentException("Updated GymClass must not be null");
        }

        GymClass existing = getByIdOrThrow(classId);

        Trainer newTrainer = updatedClass.getTrainer() != null ? updatedClass.getTrainer() : existing.getTrainer();
        DayOfWeek newDay = updatedClass.getDayOfWeek() != null ? updatedClass.getDayOfWeek() : existing.getDayOfWeek();
        LocalTime newStartTime = updatedClass.getStartTime() != null ? updatedClass.getStartTime() : existing.getStartTime();

        boolean trainerChanged = updatedClass.getTrainer() != null && !updatedClass.getTrainer().equals(existing.getTrainer());
        boolean dayChanged = updatedClass.getDayOfWeek() != null && !updatedClass.getDayOfWeek().equals(existing.getDayOfWeek());
        boolean timeChanged = updatedClass.getStartTime() != null && !updatedClass.getStartTime().equals(existing.getStartTime());

        if (trainerChanged || dayChanged || timeChanged) {
                        List<GymClass> sameSlot = gymClassRepository.findByTrainerAndDayOfWeek(newTrainer, newDay);
            boolean conflict = sameSlot.stream()
                    .anyMatch(c -> !c.getId().equals(existing.getId()) &&
                            newStartTime.equals(c.getStartTime()));
            if (conflict) {
                throw new IllegalStateException("Trainer already has a class at this day and time");
            }
        }

                existing.setTrainer(newTrainer);
        existing.setDayOfWeek(newDay);
        existing.setStartTime(newStartTime);

        if (updatedClass.getName() != null && !updatedClass.getName().isBlank()) {
            existing.setName(updatedClass.getName().trim());
        }

        if (updatedClass.getDurationMinutes() > 0) {
            existing.setDurationMinutes(updatedClass.getDurationMinutes());
        }

        if (updatedClass.getDifficulty() != null && !updatedClass.getDifficulty().isBlank()) {
            existing.setDifficulty(updatedClass.getDifficulty().trim());
        }

        if (updatedClass.getCapacity() > 0) {
            existing.setCapacity(updatedClass.getCapacity());
        }

        return gymClassRepository.save(existing);
    }

    @Override
    public void deleteClass(Long classId) {
        GymClass existing = getByIdOrThrow(classId);
                gymClassRepository.delete(existing);
    }

    
    @Override
    @Transactional(readOnly = true)
    public boolean hasTrainerConflict(Trainer trainer, DayOfWeek dayOfWeek, LocalTime startTime) {
        if (trainer == null || trainer.getId() == null || dayOfWeek == null || startTime == null) {
            return false;
        }
        List<GymClass> sameDay = gymClassRepository.findByTrainerAndDayOfWeek(trainer, dayOfWeek);
        return sameDay.stream().anyMatch(c -> startTime.equals(c.getStartTime()));
    }

    
    @Override
    @Transactional(readOnly = true)
    public long countAllClasses() {
        return gymClassRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByDifficulty(String difficulty) {
        return gymClassRepository.countByDifficulty(difficulty);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByTrainer(Trainer trainer) {
        return gymClassRepository.countByTrainer(trainer);
    }
}

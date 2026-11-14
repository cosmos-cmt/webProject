package com.cuda.gym.web.mapper;

import com.cuda.gym.domain.model.GymClass;
import com.cuda.gym.domain.model.Trainer;
import com.cuda.gym.web.dto.GymClassForm;
import com.cuda.gym.domain.repository.TrainerRepository;

public class GymClassMapper {

    private final TrainerRepository trainerRepository;

    public GymClassMapper(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

        public GymClass toGymClass(GymClassForm form) {
        if (form == null) {
            return null;
        }

        GymClass gymClass = new GymClass();
              gymClass.setName(form.getName());
        gymClass.setDayOfWeek(form.getDayOfWeek());
        gymClass.setStartTime(form.getStartTime());
        gymClass.setDurationMinutes(form.getDurationMinutes());
        gymClass.setDifficulty(form.getDifficulty());
        gymClass.setCapacity(form.getCapacity());

        Trainer trainer = resolveTrainer(form.getTrainerId());
        gymClass.setTrainer(trainer);

        return gymClass;
    }

        public void updateGymClassFromForm(GymClass gymClass, GymClassForm form) {
        if (gymClass == null || form == null) {
            return;
        }

        gymClass.setName(form.getName());
        gymClass.setDayOfWeek(form.getDayOfWeek());
        gymClass.setStartTime(form.getStartTime());
        gymClass.setDurationMinutes(form.getDurationMinutes());
        gymClass.setDifficulty(form.getDifficulty());
        gymClass.setCapacity(form.getCapacity());

        Trainer trainer = resolveTrainer(form.getTrainerId());
        gymClass.setTrainer(trainer);
    }

       public GymClassForm toGymClassForm(GymClass gymClass) {
        if (gymClass == null) {
            return null;
        }

        GymClassForm form = new GymClassForm();
        form.setId(gymClass.getId());
        form.setName(gymClass.getName());
        form.setDayOfWeek(gymClass.getDayOfWeek());
        form.setStartTime(gymClass.getStartTime());
        form.setDurationMinutes(gymClass.getDurationMinutes());
        form.setDifficulty(gymClass.getDifficulty());
        form.setCapacity(gymClass.getCapacity());

        if (gymClass.getTrainer() != null) {
            form.setTrainerId(gymClass.getTrainer().getId());
        }

        return form;
    }


    private Trainer resolveTrainer(Long trainerId) {
        if (trainerId == null) {
            return null;
        }
        return trainerRepository.findById(trainerId).orElse(null);
    }
}
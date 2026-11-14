package com.cuda.gym.web.mapper;

import com.cuda.gym.domain.model.Trainer;
import com.cuda.gym.web.dto.TrainerForm;

public class TrainerMapper {

      public Trainer toTrainer(TrainerForm form) {
        if (form == null) {
            return null;
        }
        Trainer trainer = new Trainer();
        trainer.setName(form.getName());
        trainer.setEmail(form.getEmail());
        trainer.setSpecialty(form.getSpecialty());
        trainer.setBio(form.getBio());
        trainer.setYearsExperience(form.getYearsOfExperience());
        trainer.setPhotoPath(form.getPhotoPath());
        return trainer;
    }

        public void updateTrainerFromForm(Trainer trainer, TrainerForm form) {
        if (trainer == null || form == null) {
            return;
        }
        trainer.setName(form.getName());
        trainer.setEmail(form.getEmail());
        trainer.setSpecialty(form.getSpecialty());
        trainer.setBio(form.getBio());
        trainer.setYearsExperience(form.getYearsOfExperience());
        trainer.setPhotoPath(form.getPhotoPath());
    }

        public TrainerForm toTrainerForm(Trainer trainer) {
        if (trainer == null) {
            return null;
        }
        TrainerForm form = new TrainerForm();
        form.setId(trainer.getId());
        form.setName(trainer.getName());
        form.setEmail(trainer.getEmail());
        form.setSpecialty(trainer.getSpecialty());
        form.setBio(trainer.getBio());
        form.setYearsOfExperience(trainer.getYearsExperience());
        form.setPhotoPath(trainer.getPhotoPath());
        return form;
    }
}

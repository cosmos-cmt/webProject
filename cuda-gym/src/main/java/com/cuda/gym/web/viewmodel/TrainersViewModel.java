package com.cuda.gym.web.viewmodel;

import com.cuda.gym.domain.model.GymClass;
import com.cuda.gym.domain.model.Trainer;

import java.util.ArrayList;
import java.util.List;

public class TrainersViewModel {

       private List<Trainer> trainers = new ArrayList<>();

       private Trainer selectedTrainer;

        private List<GymClass> classesForTrainer = new ArrayList<>();

        private String searchQuery;

    public TrainersViewModel() {
    }

    public List<Trainer> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers = trainers != null ? trainers : new ArrayList<>();
    }

    public Trainer getSelectedTrainer() {
        return selectedTrainer;
    }

    public void setSelectedTrainer(Trainer selectedTrainer) {
        this.selectedTrainer = selectedTrainer;
    }

    public List<GymClass> getClassesForTrainer() {
        return classesForTrainer;
    }

    public void setClassesForTrainer(List<GymClass> classesForTrainer) {
        this.classesForTrainer = classesForTrainer != null ? classesForTrainer : new ArrayList<>();
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
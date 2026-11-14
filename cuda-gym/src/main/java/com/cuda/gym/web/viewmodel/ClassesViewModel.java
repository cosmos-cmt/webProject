package com.cuda.gym.web.viewmodel;


import com.cuda.gym.domain.model.GymClass;
import com.cuda.gym.domain.model.Trainer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClassesViewModel {

        private List<GymClass> classes = new ArrayList<>();

        private List<Trainer> availableTrainers = new ArrayList<>();

       private Long selectedTrainerId;

       private DayOfWeek selectedDayOfWeek;

        private String selectedDifficulty;

        private GymClass selectedClass;

        private LocalDate selectedDate;

    public ClassesViewModel() {
    }

    public List<GymClass> getClasses() {
        return classes;
    }

    public void setClasses(List<GymClass> classes) {
        this.classes = classes != null ? classes : new ArrayList<>();
    }

    public List<Trainer> getAvailableTrainers() {
        return availableTrainers;
    }

    public void setAvailableTrainers(List<Trainer> availableTrainers) {
        this.availableTrainers = availableTrainers != null ? availableTrainers : new ArrayList<>();
    }

    public Long getSelectedTrainerId() {
        return selectedTrainerId;
    }

    public void setSelectedTrainerId(Long selectedTrainerId) {
        this.selectedTrainerId = selectedTrainerId;
    }

    public DayOfWeek getSelectedDayOfWeek() {
        return selectedDayOfWeek;
    }

    public void setSelectedDayOfWeek(DayOfWeek selectedDayOfWeek) {
        this.selectedDayOfWeek = selectedDayOfWeek;
    }

    public String getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public void setSelectedDifficulty(String selectedDifficulty) {
        this.selectedDifficulty = selectedDifficulty;
    }

    public GymClass getSelectedClass() {
        return selectedClass;
    }

    public void setSelectedClass(GymClass selectedClass) {
        this.selectedClass = selectedClass;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }
}
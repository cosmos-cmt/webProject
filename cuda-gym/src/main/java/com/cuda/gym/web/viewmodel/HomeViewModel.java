package com.cuda.gym.web.viewmodel;

import com.cuda.gym.domain.model.GymClass;
import com.cuda.gym.domain.model.Plan;
import com.cuda.gym.domain.model.Trainer;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel {

        private List<Plan> highlightPlans = new ArrayList<>();

       private List<GymClass> featuredClasses = new ArrayList<>();

        private List<Trainer> featuredTrainers = new ArrayList<>();

    public HomeViewModel() {
    }

    public List<Plan> getHighlightPlans() {
        return highlightPlans;
    }

    public void setHighlightPlans(List<Plan> highlightPlans) {
        this.highlightPlans = highlightPlans != null ? highlightPlans : new ArrayList<>();
    }

    public List<GymClass> getFeaturedClasses() {
        return featuredClasses;
    }

    public void setFeaturedClasses(List<GymClass> featuredClasses) {
        this.featuredClasses = featuredClasses != null ? featuredClasses : new ArrayList<>();
    }

    public List<Trainer> getFeaturedTrainers() {
        return featuredTrainers;
    }

    public void setFeaturedTrainers(List<Trainer> featuredTrainers) {
        this.featuredTrainers = featuredTrainers != null ? featuredTrainers : new ArrayList<>();
    }
}
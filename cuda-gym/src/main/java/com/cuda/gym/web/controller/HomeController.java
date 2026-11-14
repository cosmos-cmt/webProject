package com.cuda.gym.web.controller;

import com.cuda.gym.domain.service.GymClassService;
import com.cuda.gym.domain.service.PlanService;
import com.cuda.gym.domain.service.TrainerService;
import com.cuda.gym.web.viewmodel.HomeViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

 
@Controller
public class HomeController {

    private final PlanService planService;
    private final GymClassService gymClassService;
    private final TrainerService trainerService;

    public HomeController(PlanService planService,
                          GymClassService gymClassService,
                          TrainerService trainerService) {
        this.planService = planService;
        this.gymClassService = gymClassService;
        this.trainerService = trainerService;
    }
 
    @GetMapping({"/", "/home", "/index"})
    public String showHome(Model model) {
        HomeViewModel vm = new HomeViewModel();

         
        vm.setHighlightPlans(planService.findTopPlansForHome());

        
        vm.setFeaturedClasses(gymClassService.findFeaturedClassesForHome());

         
        vm.setFeaturedTrainers(trainerService.findFeaturedTrainersForHome());

        model.addAttribute("viewModel", vm);
        return "home/index";
    }
}
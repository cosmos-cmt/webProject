package com.cuda.gym.web.controller;

import com.cuda.gym.domain.service.TrainerService;
import com.cuda.gym.web.viewmodel.TrainersViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

 
@Controller
@RequestMapping("/trainers")
public class TrainersController {

    private final TrainerService trainerService;

    public TrainersController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

     
    @GetMapping
    public String listTrainers(@RequestParam(value = "q", required = false) String query,
                               Model model) {

        TrainersViewModel vm = new TrainersViewModel();
        vm.setSearchQuery(query);

        if (query == null || query.trim().isEmpty()) {
            vm.setTrainers(trainerService.findAll());
        } else {
            vm.setTrainers(trainerService.searchByNameOrSpecialty(query.trim()));
        }

        model.addAttribute("viewModel", vm);
        return "trainers/list";
    }

        @GetMapping("/{id}")
    public String trainerDetail(@PathVariable("id") Long id, Model model) {
        TrainersViewModel vm = new TrainersViewModel();

        vm.setSelectedTrainer(trainerService.getByIdOrThrow(id));
        vm.setClassesForTrainer(trainerService.findClassesForTrainer(id));

        model.addAttribute("viewModel", vm);
        return "trainers/detail";
    }
}
package com.cuda.gym.web.controller;

import com.cuda.gym.domain.model.*;
import com.cuda.gym.domain.service.classmgmt.GymClassService;
import com.cuda.gym.domain.service.trainer.TrainerService;
import com.cuda.gym.web.dto.GymClassForm;
import com.cuda.gym.web.viewmodel.ClassesViewModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Controller
@RequestMapping("/classes")
public class ClassesController {

    private final GymClassService gymClassService;
    private final TrainerService trainerService;

    public ClassesController(GymClassService gymClassService,
                             TrainerService trainerService) {
        this.gymClassService = gymClassService;
        this.trainerService = trainerService;
    }
    @GetMapping
    public String listClasses(@RequestParam(value = "trainerId", required = false) Long trainerId,
                              @RequestParam(value = "dayOfWeek", required = false) DayOfWeek dayOfWeek,
                              @RequestParam(value = "difficulty", required = false) String difficulty,
                              Model model) {

        ClassesViewModel vm = new ClassesViewModel();

         
        vm.setAvailableTrainers(trainerService.findAll());
        vm.setSelectedTrainerId(trainerId);
        vm.setSelectedDayOfWeek(dayOfWeek);
        vm.setSelectedDifficulty(difficulty);

         
        if (trainerId == null && dayOfWeek == null && difficulty == null) {
            vm.setClasses(gymClassService.findAll());
        } else {
            vm.setClasses(
                    gymClassService.findByTrainerAndDifficulty(trainerId,difficulty,dayOfWeek)
            );
        }

        model.addAttribute("viewModel", vm);
        return "classes/list";
    }

    
    @GetMapping("/{id}")
    public String classDetail(@PathVariable("id") Long id,
                              @RequestParam(value = "date", required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              Model model) {

        ClassesViewModel vm = new ClassesViewModel();

        vm.setSelectedClass(gymClassService.getByIdOrThrow(id));
        vm.setSelectedDate(date != null ? date : LocalDate.now());

        model.addAttribute("viewModel", vm);
        return "classes/detail";
    }
}
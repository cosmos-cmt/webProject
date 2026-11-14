package com.cuda.gym.web.controller.admin;

import com.cuda.gym.domain.model.GymClass;
import com.cuda.gym.domain.model.Trainer;
import com.cuda.gym.domain.service.ClassScheduleService;
import com.cuda.gym.domain.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/classes")
public class AdminClassController {

    private final ClassScheduleService classScheduleService;
    private final TrainerService trainerService;

    public AdminClassController(ClassScheduleService classScheduleService,
                                TrainerService trainerService) {
        this.classScheduleService = classScheduleService;
        this.trainerService = trainerService;
    }

        @GetMapping
    public String listClasses(Model model) {
        model.addAttribute("classes", classScheduleService.findAll());
        model.addAttribute("trainers", trainerService.findAll());

        if (!model.containsAttribute("classForm")) {
            model.addAttribute("classForm", new GymClass());
        }

        return "admin-classes";
    }

       @PostMapping
    public String createClass(@Valid @ModelAttribute("classForm") GymClass classForm,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("classes", classScheduleService.findAll());
            model.addAttribute("trainers", trainerService.findAll());
            return "admin-classes";
        }

        Trainer trainer = trainerService.findById(classForm.getTrainer().getId());
        classForm.setTrainer(trainer);

        classScheduleService.createOrUpdate(classForm);
        return "redirect:/admin/classes";
    }

        @GetMapping("/{id}/edit")
    public String editClass(@PathVariable Long id, Model model) {
        GymClass clazz = classScheduleService.findById(id);

        model.addAttribute("classes", classScheduleService.findAll());
        model.addAttribute("trainers", trainerService.findAll());
        model.addAttribute("classForm", clazz);

        return "admin-classes";
    }

       @PostMapping("/{id}")
    public String updateClass(@PathVariable Long id,
                              @Valid @ModelAttribute("classForm") GymClass classForm,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("classes", classScheduleService.findAll());
            model.addAttribute("trainers", trainerService.findAll());
            return "admin-classes";
        }

        classForm.setId(id);
        Trainer trainer = trainerService.findById(classForm.getTrainer().getId());
        classForm.setTrainer(trainer);

        classScheduleService.createOrUpdate(classForm);
        return "redirect:/admin/classes";
    }

        @PostMapping("/{id}/delete")
    public String deleteClass(@PathVariable Long id) {
        classScheduleService.delete(id);
        return "redirect:/admin/classes";
    }
}
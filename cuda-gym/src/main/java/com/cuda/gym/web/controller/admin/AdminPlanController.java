package com.cuda.gym.web.controller.admin;

import com.cuda.gym.domain.model.Plan;
import com.cuda.gym.domain.service.PlanService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/plans")
public class AdminPlanController {

    private final PlanService planService;

    public AdminPlanController(PlanService planService) {
        this.planService = planService;
    }

        @GetMapping
    public String listPlans(Model model) {
        model.addAttribute("plans", planService.findAll());   // all (active + inactive) or adapt
        if (!model.containsAttribute("planForm")) {
            model.addAttribute("planForm", new Plan());
        }
        return "admin-plans";
    }

    
    @PostMapping
    public String createPlan(@Valid @ModelAttribute("planForm") Plan planForm,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("plans", planService.findAll());
            return "admin-plans";
        }

        planForm.setActive(true);
        planService.createOrUpdate(planForm);
        return "redirect:/admin/plans";
    }

        @GetMapping("/{id}/edit")
    public String editPlan(@PathVariable Long id, Model model) {
        Plan plan = planService.findById(id);
        model.addAttribute("plans", planService.findAll());
        model.addAttribute("planForm", plan);
        return "admin-plans";
    }

        @PostMapping("/{id}")
    public String updatePlan(@PathVariable Long id,
                             @Valid @ModelAttribute("planForm") Plan planForm,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("plans", planService.findAll());
            return "admin-plans";
        }

        planForm.setId(id);
        planService.createOrUpdate(planForm);
        return "redirect:/admin/plans";
    }

        @PostMapping("/{id}/delete")
    public String deletePlan(@PathVariable Long id) {
        planService.delete(id);   // implementation can mark inactive instead of physical delete
        return "redirect:/admin/plans";
    }
}
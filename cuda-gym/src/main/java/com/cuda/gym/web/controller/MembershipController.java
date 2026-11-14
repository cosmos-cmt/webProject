package com.cuda.gym.web.controller;

import com.cuda.gym.domain.model.Plan;
import com.cuda.gym.domain.model.User;
import com.cuda.gym.domain.service.membership.MembershipService;
import com.cuda.gym.domain.service.plan.PlanService;
import com.cuda.gym.domain.service.UserService;
import com.cuda.gym.web.dto.MembershipForm;
import com.cuda.gym.web.viewmodel.MembershipViewModel;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

 
@Controller
@RequestMapping("/membership")
public class MembershipController {

    private final PlanService planService;
    private final MembershipService membershipService;
    private final UserService userService;

    public MembershipController(PlanService planService,
                                MembershipService membershipService,
                                UserService userService) {
        this.planService = planService;
        this.membershipService = membershipService;
        this.userService = userService;
    }

     
    @GetMapping
    public String showMembershipPage(Model model) {
        MembershipViewModel vm = new MembershipViewModel();

         
        vm.setPlans(planService.findAll());

         
        if (!model.containsAttribute("membershipForm")) {
            MembershipForm form = new MembershipForm();
            vm.setMembershipForm(form);
            model.addAttribute("membershipForm", form);
        }

        model.addAttribute("viewModel", vm);
        return "membership/index";
    }

     
    @PostMapping("/register")
    public String registerMembership(@Valid @ModelAttribute("membershipForm") MembershipForm form,
                                     BindingResult bindingResult,
                                     Model model,
                                     @AuthenticationPrincipal UserDetails currentUserDetails) {

         
        if (bindingResult.hasErrors()) {
            MembershipViewModel vm = new MembershipViewModel();
            vm.setPlans(planService.findAll());
            vm.setMembershipForm(form);
            model.addAttribute("viewModel", vm);
            return "membership/index";
        }

         
        Plan plan = planService.getByIdOrThrow(form.getPlanId());
 
        User member;
        if (currentUserDetails != null) {
            member = userService.findByEmail(currentUserDetails.getUsername());
        } else {
             
            member = membershipService.registerNewMember(form, plan);
        }

         
        membershipService.createMembershipForUser(member, plan);

         
        return "redirect:/profile";
    }
}
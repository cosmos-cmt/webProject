package com.cuda.gym.web.controller.admin;

import com.cuda.gym.domain.model.Membership;
import com.cuda.gym.domain.model.Plan;
import com.cuda.gym.domain.model.User;
import com.cuda.gym.domain.model.UserRole;
import com.cuda.gym.domain.service.MembershipService;
import com.cuda.gym.domain.service.PlanService;
import com.cuda.gym.domain.service.UserService;
import com.cuda.gym.web.dto.MemberAdminForm;
import com.cuda.gym.web.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final UserService userService;
    private final PlanService planService;
    private final MembershipService membershipService;
    private final UserMapper userMapper;

    public AdminMemberController(UserService userService,
                                 PlanService planService,
                                 MembershipService membershipService,
                                 UserMapper userMapper) {
        this.userService = userService;
        this.planService = planService;
        this.membershipService = membershipService;
        this.userMapper = userMapper;
    }

    
    @GetMapping
    public String listMembers(@RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "10") int size,
                              @RequestParam(name = "plan", required = false) Long planId,
                              @RequestParam(name = "status", required = false) String status,
                              @RequestParam(name = "from", required = false) LocalDate fromDate,
                              @RequestParam(name = "to", required = false) LocalDate toDate,
                              Model model) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> memberPage = userService.searchMembers(planId, status, fromDate, toDate, pageRequest);

        model.addAttribute("memberPage", memberPage);
        model.addAttribute("selectedPlanId", planId);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        List<Plan> allPlans = planService.findAllPlans();
        model.addAttribute("plans", allPlans);

        return "admin/members/list";     }

    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("memberAdminForm")) {
            model.addAttribute("memberAdminForm", new MemberAdminForm());
        }
        model.addAttribute("roles", UserRole.values());
        return "admin/members/form";     }

    @PostMapping("/new")
    public String createMember(@Valid @ModelAttribute("memberAdminForm") MemberAdminForm form,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {

                if (!bindingResult.hasFieldErrors("email") &&
                userService.emailExists(form.getEmail())) {
            bindingResult.rejectValue("email", "email.exists",
                    "An account with this email already exists");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.memberAdminForm", bindingResult);
            redirectAttributes.addFlashAttribute("memberAdminForm", form);
            return "redirect:/admin/members/new";
        }

        User user = new User();
        user.setFullName(form.getFullName());
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());
        user.setDateOfBirth(form.getDateOfBirth());
        user.setRole(form.getRole());
                userService.createMemberWithGeneratedPassword(user);

        redirectAttributes.addFlashAttribute("memberCreated", true);
        return "redirect:/admin/members";
    }

  
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found");
            return "redirect:/admin/members";
        }

        if (!model.containsAttribute("memberAdminForm")) {
            MemberAdminForm form = new MemberAdminForm();
            form.setId(user.getId());
            form.setFullName(user.getFullName());
            form.setEmail(user.getEmail());
            form.setPhone(user.getPhone());
            form.setDateOfBirth(user.getDateOfBirth());
            form.setRole(user.getRole());
            form.setBanned(user.isBanned());
            model.addAttribute("memberAdminForm", form);
        }
        model.addAttribute("roles", UserRole.values());
        return "admin/members/form";
    }

    @PostMapping("/{id}/edit")
    public String updateMember(@PathVariable("id") Long id,
                               @Valid @ModelAttribute("memberAdminForm") MemberAdminForm form,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        User user = userService.findById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found");
            return "redirect:/admin/members";
        }

                if (!bindingResult.hasFieldErrors("email") &&
                userService.emailBelongsToAnotherUser(form.getEmail(), id)) {
            bindingResult.rejectValue("email", "email.exists",
                    "Another account already uses this email");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.memberAdminForm", bindingResult);
            redirectAttributes.addFlashAttribute("memberAdminForm", form);
            return "redirect:/admin/members/" + id + "/edit";
        }

        user.setFullName(form.getFullName());
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());
        user.setDateOfBirth(form.getDateOfBirth());
        user.setRole(form.getRole());
        user.setBanned(form.isBanned());

        userService.save(user);

        redirectAttributes.addFlashAttribute("memberUpdated", true);
        return "redirect:/admin/members";
    }

   
    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable("id") Long id,
                               RedirectAttributes redirectAttributes) {
        User user = userService.findById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found");
            return "redirect:/admin/members";
        }

        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("memberDeleted", true);
        return "redirect:/admin/members";
    }

    @PostMapping("/{id}/ban")
    public String banMember(@PathVariable("id") Long id,
                            RedirectAttributes redirectAttributes) {
        User user = userService.findById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found");
            return "redirect:/admin/members";
        }

        userService.banUser(user);
        redirectAttributes.addFlashAttribute("memberBanned", true);
        return "redirect:/admin/members";
    }

   
    @GetMapping("/{id}/subscriptions")
    public String editSubscription(@PathVariable("id") Long id,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        User user = userService.findById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found");
            return "redirect:/admin/members";
        }

        Membership current = membershipService.findCurrentMembershipForUser(id);
        List<Plan> plans = planService.findAllPlans();

        model.addAttribute("member", user);
        model.addAttribute("currentMembership", current);
        model.addAttribute("plans", plans);

        return "admin/members/subscription";
    }

    @PostMapping("/{id}/subscriptions/assign")
    public String assignSubscription(@PathVariable("id") Long id,
                                     @RequestParam("planId") Long planId,
                                     @RequestParam("startDate") LocalDate startDate,
                                     RedirectAttributes redirectAttributes) {

        User user = userService.findById(id);
        Plan plan = planService.findById(planId);

        if (user == null || plan == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member or plan not found");
            return "redirect:/admin/members";
        }

        membershipService.assignNewMembership(user, plan, startDate);

        redirectAttributes.addFlashAttribute("subscriptionUpdated", true);
        return "redirect:/admin/members/" + id + "/subscriptions";
    }

    @PostMapping("/{id}/subscriptions/extend")
    public String extendSubscription(@PathVariable("id") Long id,
                                     @RequestParam("months") int months,
                                     RedirectAttributes redirectAttributes) {

        Membership current = membershipService.findCurrentMembershipForUser(id);
        if (current == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "No active membership to extend");
            return "redirect:/admin/members/" + id + "/subscriptions";
        }

        membershipService.extendMembership(current, months);
        redirectAttributes.addFlashAttribute("subscriptionUpdated", true);
        return "redirect:/admin/members/" + id + "/subscriptions";
    }
}
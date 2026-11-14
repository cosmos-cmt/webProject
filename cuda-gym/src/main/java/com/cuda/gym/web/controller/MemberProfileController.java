package com.cuda.gym.web.controller;

import com.cuda.gym.domain.model.ClassBooking;
import com.cuda.gym.domain.model.Membership;
import com.cuda.gym.domain.model.User;
import com.cuda.gym.domain.service.BookingService;
import com.cuda.gym.domain.service.MembershipService;
import com.cuda.gym.domain.service.UserService;
import com.cuda.gym.web.dto.MemberEditForm;
import com.cuda.gym.web.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

 
@Controller
public class MemberProfileController {

    private final UserService userService;
    private final MembershipService membershipService;
    private final BookingService bookingService;
    private final UserMapper userMapper;

    public MemberProfileController(UserService userService,
                                   MembershipService membershipService,
                                   BookingService bookingService,
                                   UserMapper userMapper) {
        this.userService = userService;
        this.membershipService = membershipService;
        this.bookingService = bookingService;
        this.userMapper = userMapper;
    }

     
    @GetMapping("/member/profile")
    public String showProfile(Model model, Principal principal) {
        User currentUser = getCurrentUser(principal);
        if (currentUser == null) {
            return "redirect:/login";
        }

        if (!model.containsAttribute("memberEditForm")) {
            MemberEditForm form = userMapper.toMemberEditForm(currentUser);
            model.addAttribute("memberEditForm", form);
        }

         
        Membership currentMembership = membershipService.findCurrentMembershipForUser(currentUser.getId());
        model.addAttribute("currentMembership", currentMembership);

         
        List<ClassBooking> upcomingBookings =
                bookingService.findUpcomingBookingsForUser(currentUser.getId());
        List<ClassBooking> pastBookings =
                bookingService.findPastBookingsForUser(currentUser.getId());

        model.addAttribute("upcomingBookings", upcomingBookings);
        model.addAttribute("pastBookings", pastBookings);

        return "member/profile"; // member/profile.html
    }

    
    @PostMapping("/member/profile")
    public String updateProfile(@Valid @ModelAttribute("memberEditForm") MemberEditForm form,
                                BindingResult bindingResult,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {

        User currentUser = getCurrentUser(principal);
        if (currentUser == null) {
            return "redirect:/login";
        }

         
        if (form.isPasswordChangeRequested()) {
           
            if (!form.isNewPasswordConfirmed()) {
                bindingResult.rejectValue("confirmNewPassword", "password.mismatch",
                        "New passwords do not match");
            }

             
            if (form.getCurrentPassword() == null ||
                    !userService.isCurrentPasswordValid(currentUser, form.getCurrentPassword())) {
                bindingResult.rejectValue("currentPassword", "password.invalid",
                        "Current password is incorrect");
            }
        }
 
        if (!bindingResult.hasFieldErrors("email") &&
                userService.emailBelongsToAnotherUser(form.getEmail(), currentUser.getId())) {
            bindingResult.rejectValue("email", "email.exists",
                    "Another account already uses this email");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.memberEditForm", bindingResult);
            redirectAttributes.addFlashAttribute("memberEditForm", form);
            redirectAttributes.addFlashAttribute("profileError", true);
            return "redirect:/member/profile";
        }

         
        userMapper.updateUserFromMemberEditForm(currentUser, form);
        userMapper.updatePasswordIfRequested(currentUser, form);
        userService.save(currentUser);

        redirectAttributes.addFlashAttribute("profileUpdated", true);
        return "redirect:/member/profile";
    }

     
    private User getCurrentUser(Principal principal) {
        if (principal == null) {
            return null;
        }
        return userService.findByEmail(principal.getName());
    }
}
package com.cuda.gym.web.controller;

import com.cuda.gym.domain.model.User;
import com.cuda.gym.domain.service.AuthService;
import com.cuda.gym.web.dto.LoginForm;
import com.cuda.gym.web.dto.RegistrationForm;
import com.cuda.gym.web.mapper.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    public AuthController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

        @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("registrationForm")) {
            model.addAttribute("registrationForm", new RegistrationForm());
        }
        return "auth/register";     }

    @PostMapping("/register")
    public String handleRegistration(@Valid @ModelAttribute("registrationForm") RegistrationForm form,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

                if (!form.isPasswordConfirmed()) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch",
                    "Passwords do not match");
        }

                if (form.getDateOfBirth() != null && !authService.isAgeValid(form.getDateOfBirth(), 16)) {
            bindingResult.rejectValue("dateOfBirth", "age.invalid",
                    "You must be at least 16 years old to register");
        }

        
        if (!bindingResult.hasFieldErrors("email") &&
                authService.emailExists(form.getEmail())) {
            bindingResult.rejectValue("email", "email.exists",
                    "An account with this email already exists");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.registrationForm", bindingResult);
            redirectAttributes.addFlashAttribute("registrationForm", form);
            return "redirect:/register";
        }

                User user = userMapper.toUser(form);
        authService.register(user);

      
        redirectAttributes.addFlashAttribute("registrationSuccess", true);
        return "redirect:/login";
    }

   
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "auth/login"; 
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("loginForm") LoginForm form,
                              BindingResult bindingResult,
                              HttpServletResponse response,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.loginForm", bindingResult);
            redirectAttributes.addFlashAttribute("loginForm", form);
            return "redirect:/login";
        }

         
        User user = authService.authenticate(form.getEmail(), form.getPassword());
        if (user == null) {
            bindingResult.reject("login.invalid", "Invalid email or password");
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.loginForm", bindingResult);
            redirectAttributes.addFlashAttribute("loginForm", form);
            return "redirect:/login";
        }

         
        authService.createSession(user);

         
        if (form.isRememberMe()) {
            String token = authService.createRememberMeTokenFor(user);
            Cookie cookie = new Cookie("REMEMBER_ME", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);
        }

         
        if (authService.isAdmin(user)) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/member/profile";
    }

    
    @PostMapping("/logout")
    public String handleLogout(HttpServletResponse response) {
        authService.logoutCurrentUser();

         
        Cookie cookie = new Cookie("REMEMBER_ME", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }
}

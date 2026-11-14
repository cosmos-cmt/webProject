package com.cuda.gym.web.controller;

import com.cuda.gym.web.dto.ContactForm;
import com.cuda.gym.web.viewmodel.ContactViewModel;
import com.cuda.gym.domain.model.ContactMessage;
import com.cuda.gym.domain.service.ContactMessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalTime;

 
@Controller
public class ContactController {

    private final ContactMessageService contactMessageService;

    @Value("${gym.contact.address:123 Fitness Street, City}")
    private String gymAddress;

    @Value("${gym.contact.phone:+1 555 123 456}")
    private String gymPhone;

    @Value("${gym.contact.email:info@gym-example.com}")
    private String gymEmail;

    public ContactController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @GetMapping("/contact")
    public String showContactPage(Model model) {
        if (!model.containsAttribute("contactForm")) {
            model.addAttribute("contactForm", new ContactForm());
        }

        ContactViewModel vm = buildViewModel(false, false);
        model.addAttribute("viewModel", vm);

        return "contact"; // contact.html
    }

    @PostMapping("/contact")
    public String handleContactSubmit(@Valid @ModelAttribute("contactForm") ContactForm form,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
             
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contactForm",
                    bindingResult);
            redirectAttributes.addFlashAttribute("contactForm", form);

             
            redirectAttributes.addFlashAttribute("contactError", true);
            return "redirect:/contact";
        }

         
        message.setName(form.getName());
        message.setEmail(form.getEmail());
        message.setSubject(form.getSubject());
        message.setMessage(form.getMessage());
        contactMessageService.save(message);

         
        redirectAttributes.addFlashAttribute("contactSuccess", true);

        return "redirect:/contact";
    }

     
    private ContactViewModel buildViewModel(boolean success, boolean error) {
        ContactViewModel vm = new ContactViewModel();
        vm.setGymAddress(gymAddress);
        vm.setGymPhone(gymPhone);
        vm.setGymEmail(gymEmail);

         
        vm.addOpeningHours(DayOfWeek.MONDAY, LocalTime.of(6, 0), LocalTime.of(22, 0));
        vm.addOpeningHours(DayOfWeek.TUESDAY, LocalTime.of(6, 0), LocalTime.of(22, 0));
        vm.addOpeningHours(DayOfWeek.WEDNESDAY, LocalTime.of(6, 0), LocalTime.of(22, 0));
        vm.addOpeningHours(DayOfWeek.THURSDAY, LocalTime.of(6, 0), LocalTime.of(22, 0));
        vm.addOpeningHours(DayOfWeek.FRIDAY, LocalTime.of(6, 0), LocalTime.of(22, 0));
        vm.addOpeningHours(DayOfWeek.SATURDAY, LocalTime.of(8, 0), LocalTime.of(20, 0));
        vm.addOpeningHours(DayOfWeek.SUNDAY, null, null); // Closed

        vm.setShowSuccessMessage(success);
        vm.setShowErrorMessage(error);

        return vm;
    }

     
    @ModelAttribute("viewModel")
    public ContactViewModel contactViewModel(Model model) {
        boolean success = Boolean.TRUE.equals(model.getAttribute("contactSuccess"));
        boolean error = Boolean.TRUE.equals(model.getAttribute("contactError"));
        ContactViewModel vm = buildViewModel(success, error);
        if (model.getAttribute("contactForm") instanceof ContactForm form) {
            vm.setContactForm(form);
        }
        return vm;
    }
}
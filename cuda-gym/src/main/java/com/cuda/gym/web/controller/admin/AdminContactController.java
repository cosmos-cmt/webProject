package com.cuda.gym.web.controller.admin;

import com.cuda.gym.domain.model.ContactMessage;
import com.cuda.gym.domain.service.ContactMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/contact-messages")
public class AdminContactController {

    private final ContactMessageService contactMessageService;

    public AdminContactController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

   
    @GetMapping
    public String listMessages(@RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "10") int size,
                               Model model) {

        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.by("submittedAt").descending());
        Page<ContactMessage> messagePage =
                contactMessageService.findAll(pageRequest);

        model.addAttribute("messagePage", messagePage);
        model.addAttribute("unreadCount", contactMessageService.countUnread());

        return "admin/contact/list";     }

 
    @GetMapping("/{id}")
    public String viewMessage(@PathVariable("id") Long id,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        ContactMessage message = contactMessageService.findById(id);
        if (message == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Message not found");
            return "redirect:/admin/contact-messages";
        }

        model.addAttribute("message", message);
        model.addAttribute("unreadCount", contactMessageService.countUnread());

                if (!message.isRead()) {
            contactMessageService.markAsRead(message);
        }

        return "admin/contact/detail";
    }

    
    @PostMapping("/{id}/read")
    public String markAsRead(@PathVariable("id") Long id,
                             RedirectAttributes redirectAttributes) {
        ContactMessage message = contactMessageService.findById(id);
        if (message == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Message not found");
            return "redirect:/admin/contact-messages";
        }

        contactMessageService.markAsRead(message);
        redirectAttributes.addFlashAttribute("statusUpdated", true);
        return "redirect:/admin/contact-messages";
    }

    @PostMapping("/{id}/unread")
    public String markAsUnread(@PathVariable("id") Long id,
                               RedirectAttributes redirectAttributes) {
        ContactMessage message = contactMessageService.findById(id);
        if (message == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Message not found");
            return "redirect:/admin/contact-messages";
        }

        contactMessageService.markAsUnread(message);
        redirectAttributes.addFlashAttribute("statusUpdated", true);
        return "redirect:/admin/contact-messages";
    }

        @PostMapping("/{id}/delete")
    public String deleteMessage(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {
        ContactMessage message = contactMessageService.findById(id);
        if (message == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Message not found");
            return "redirect:/admin/contact-messages";
        }

        contactMessageService.delete(id);
        redirectAttributes.addFlashAttribute("messageDeleted", true);
        return "redirect:/admin/contact-messages";
    }

    

    @ModelAttribute("contactUnreadCount")
    public long contactUnreadCount() {
        return contactMessageService.countUnread();
    }
}
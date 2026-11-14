package com.cuda.gym.web.viewmodel;

import com.cuda.gym.web.dto.ContactForm;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class ContactViewModel {
    private ContactForm contactForm;

   
    private String gymAddress;

       private String gymPhone;

       private String gymEmail;

        private Map<DayOfWeek, String> openingHours = new LinkedHashMap<>();

        private boolean showSuccessMessage;

       private boolean showErrorMessage;

    public ContactViewModel() {
    }

    public ContactForm getContactForm() {
        return contactForm;
    }

    public void setContactForm(ContactForm contactForm) {
        this.contactForm = contactForm;
    }

    public String getGymAddress() {
        return gymAddress;
    }

    public void setGymAddress(String gymAddress) {
        this.gymAddress = gymAddress;
    }

    public String getGymPhone() {
        return gymPhone;
    }

    public void setGymPhone(String gymPhone) {
        this.gymPhone = gymPhone;
    }

    public String getGymEmail() {
        return gymEmail;
    }

    public void setGymEmail(String gymEmail) {
        this.gymEmail = gymEmail;
    }

    public Map<DayOfWeek, String> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(Map<DayOfWeek, String> openingHours) {
        this.openingHours = openingHours != null ? openingHours : new LinkedHashMap<>();
    }

    public boolean isShowSuccessMessage() {
        return showSuccessMessage;
    }

    public void setShowSuccessMessage(boolean showSuccessMessage) {
        this.showSuccessMessage = showSuccessMessage;
    }

    public boolean isShowErrorMessage() {
        return showErrorMessage;
    }

    public void setShowErrorMessage(boolean showErrorMessage) {
        this.showErrorMessage = showErrorMessage;
    }
    public void addOpeningHours(DayOfWeek day, LocalTime open, LocalTime close) {
        if (this.openingHours == null) {
            this.openingHours = new LinkedHashMap<>();
        }
        String value = (open != null && close != null)
                ? String.format("%02d:%02d - %02d:%02d",
                open.getHour(), open.getMinute(), close.getHour(), close.getMinute())
                : "Closed";
        this.openingHours.put(day, value);
    }
}
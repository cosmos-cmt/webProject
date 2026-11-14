package com.cuda.gym.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TrainerForm {

       private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Specialty is required")
    @Size(min = 3, max = 100, message = "Specialty must be between 3 and 100 characters")
    private String specialty;

    @NotBlank(message = "Bio is required")
    @Size(min = 20, max = 1000, message = "Bio must be between 20 and 1000 characters")
    private String bio;

    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Years of experience must be 0 or more")
    private Integer yearsOfExperience;

       @Size(max = 255, message = "Photo path must be at most 255 characters")
    private String photoPath;

    public TrainerForm() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty != null ? specialty.trim() : null;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio != null ? bio.trim() : null;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath != null ? photoPath.trim() : null;
    }
}

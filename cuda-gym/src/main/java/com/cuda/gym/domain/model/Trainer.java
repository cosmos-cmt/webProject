package com.cuda.gym.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
    name = "trainers",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_trainers_email", columnNames = "email")
    }
)
public class Trainer{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "name", nullable = false, length = 120)
    @NotBlank(message = "{trainer.name.notBlank}")
    @Size(max = 120, message = "{trainer.name.size}")
    private String name;

        @Column(name = "email", nullable = false, length = 160)
    @NotBlank(message = "{trainer.email.notBlank}")
    @Email(message = "{trainer.email.valid}")
    @Size(max = 160, message = "{trainer.email.size}")
    private String email;

        @Column(name = "specialty", nullable = false, length = 100)
    @NotBlank(message = "{trainer.specialty.notBlank}")
    @Size(max = 100, message = "{trainer.specialty.size}")
    private String specialty;

    
    @Column(name = "bio", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "{trainer.bio.notBlank}")
    private String bio;

        @Column(name = "photo_path", length = 255)
    @Size(max = 255, message = "{trainer.photoPath.size}")
    private String photoPath;

    @Column(name = "years_experience", nullable = false)
    @Min(value = 0, message = "{trainer.yearsExperience.min}")
    @Max(value = 80, message = "{trainer.yearsExperience.max}")
    private int yearsExperience;



    @OneToMany(
        mappedBy = "trainer",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<GymClass> classes = new HashSet<>();

    

    public Trainer() {
            }

    public Trainer(
        String name,
        String email,
        String specialty,
        String bio,
        String photoPath,
        int yearsExperience
    ) {
        this.name = name;
        this.email = email;
        this.specialty = specialty;
        this.bio = bio;
        this.photoPath = photoPath;
        this.yearsExperience = yearsExperience;
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
        this.name = name;
    }

    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(int yearsExperience) {
        this.yearsExperience = yearsExperience;
    }

    public Set<GymClass> getClasses() {
        return classes;
    }

    public void setClasses(Set<GymClass> classes) {
        this.classes = classes;
    }

    
    public void addClass(GymClass gymClass) {
        classes.add(gymClass);
        gymClass.setTrainer(this);
    }

    public void removeClass(GymClass gymClass) {
        classes.remove(gymClass);
        gymClass.setTrainer(null);
    }

    
   
    @Transient
    public String getDisplayLabel() {
        return name + " \u00b7 " + specialty;
    }

    @Transient
    public String getSeniorityLevel() {
        if (yearsExperience >= 10) {
            return "Expert";
        } else if (yearsExperience >= 5) {
            return "Senior";
        } else if (yearsExperience >= 2) {
            return "Intermediate";
        } else {
            return "Junior";
        }
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainer trainer)) return false;

        if (id != null && trainer.id != null) {
            return Objects.equals(id, trainer.id);
        }
        return Objects.equals(email, trainer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    
    @Override
    public String toString() {
        return "Trainer{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", specialty='" + specialty + '\'' +
            ", yearsExperience=" + yearsExperience +
            '}';
    }
}
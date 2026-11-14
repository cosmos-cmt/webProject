package com.cuda.gym.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email")
    }
)
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "full_name", nullable = false, length = 120)
    @NotBlank(message = "{user.fullName.notBlank}")
    @Size(max = 120, message = "{user.fullName.size}")
    private String fullName;

    
    @Column(name = "email", nullable = false, length = 160)
    @NotBlank(message = "{user.email.notBlank}")
    @Email(message = "{user.email.valid}")
    @Size(max = 160, message = "{user.email.size}")
    private String email;

    
    @Column(name = "password_hash", nullable = false, length = 255)
    @NotBlank(message = "{user.passwordHash.notBlank}")
    @Size(max = 255, message = "{user.passwordHash.size}")
    private String passwordHash;

   
    @Column(name = "phone", nullable = false, length = 20)
    @NotBlank(message = "{user.phone.notBlank}")
    @Pattern(
        regexp = "^[0-9]{8,15}$",
        message = "{user.phone.pattern}"
    )
    @Size(max = 20, message = "{user.phone.size}")
    private String phone;

   
    @Column(name = "dob", nullable = false)
    @NotNull(message = "{user.dob.notNull}")
    private LocalDate dateOfBirth;

    
    @Column(name = "role", nullable = false, length = 20)
    @NotBlank(message = "{user.role.notBlank}")
    @Size(max = 20, message = "{user.role.size}")
    private String role;

    
    @Column(name = "account_status", nullable = false, length = 20)
    @NotBlank(message = "{user.accountStatus.notBlank}")
    @Size(max = 20, message = "{user.accountStatus.size}")
    private String accountStatus;

    

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<Membership> memberships = new HashSet<>();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<ClassBooking> classBookings = new HashSet<>();

    

    public User() {
            }

    public User(
        String fullName,
        String email,
        String passwordHash,
        String phone,
        LocalDate dateOfBirth,
        String role,
        String accountStatus
    ) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.accountStatus = accountStatus;
    }

    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Set<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<Membership> memberships) {
        this.memberships = memberships;
    }

    public Set<ClassBooking> getClassBookings() {
        return classBookings;
    }

    public void setClassBookings(Set<ClassBooking> classBookings) {
        this.classBookings = classBookings;
    }

   

    public void addMembership(Membership membership) {
        memberships.add(membership);
        membership.setUser(this);
    }

    public void removeMembership(Membership membership) {
        memberships.remove(membership);
        membership.setUser(null);
    }

    public void addClassBooking(ClassBooking booking) {
        classBookings.add(booking);
        booking.setUser(this);
    }

    public void removeClassBooking(ClassBooking booking) {
        classBookings.remove(booking);
        booking.setUser(null);
    }

    @Transient
    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

        @Transient
    public boolean isAtLeastAge(int requiredAge) {
        return getAge() >= requiredAge;
    }

   

    @Transient
    public boolean isAdmin() {
        return "ROLE_ADMIN".equalsIgnoreCase(this.role);
    }

    @Transient
    public boolean isMember() {
        return "ROLE_MEMBER".equalsIgnoreCase(this.role);
    }

   
    @Transient
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.accountStatus);
    }

    @Transient
    public boolean isBanned() {
        return "BANNED".equalsIgnoreCase(this.accountStatus);
    }

   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
                if (id != null && user.id != null) {
            return Objects.equals(id, user.id);
        }
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", fullName='" + fullName + '\'' +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", role='" + role + '\'' +
            ", accountStatus='" + accountStatus + '\'' +
            '}';
    }


}
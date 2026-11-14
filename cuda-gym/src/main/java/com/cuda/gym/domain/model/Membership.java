package com.cuda.gym.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(
    name = "memberships",
    indexes = {
        @Index(name = "idx_memberships_user", columnList = "user_id"),
        @Index(name = "idx_memberships_plan", columnList = "plan_id"),
        @Index(name = "idx_memberships_status", columnList = "status")
    }
)
public class Membership  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_memberships_users")
    )
    @NotNull(message = "{membership.user.notNull}")
    private User user;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "plan_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_memberships_plans")
    )
    @NotNull(message = "{membership.plan.notNull}")
    private Plan plan;


    @Column(name = "start_date", nullable = false)
    @NotNull(message = "{membership.startDate.notNull}")
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @NotNull(message = "{membership.endDate.notNull}")
    private LocalDate endDate;

    @Column(name = "status", nullable = false, length = 20)
    @NotBlank(message = "{membership.status.notBlank}")
    @Size(max = 20, message = "{membership.status.size}")
    private String status;

  
    public Membership() {
   
    }

    public Membership(
        User user,
        Plan plan,
        LocalDate startDate,
        LocalDate endDate,
        String status
    ) {
        this.user = user;
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    @Transient
    public boolean isCurrentlyActive() {
        LocalDate today = LocalDate.now();
        return "ACTIVE".equalsIgnoreCase(status)
            && (startDate == null || !today.isBefore(startDate))
            && (endDate == null || !today.isAfter(endDate));
    }

    @Transient
    public boolean isExpiredByDate() {
        LocalDate today = LocalDate.now();
        return endDate != null && today.isAfter(endDate);
    }

    @Transient
    public String getDisplayLabel() {
        String planName = (plan != null ? plan.getDisplayName() : "Unknown Plan");
        return planName + " - " + status;
    }

    @Transient
    public boolean hasValidDateRange() {
        if (startDate == null || endDate == null) {
            return false;
        }
        return !endDate.isBefore(startDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Membership that)) return false;

        if (id != null && that.id != null) {
            return Objects.equals(id, that.id);
        }

        return Objects.equals(user, that.user)
            && Objects.equals(plan, that.plan)
            && Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, plan, startDate);
    }

   
    @Override
    public String toString() {
        return "Membership{" +
            "id=" + id +
            ", userId=" + (user != null ? user.getId() : null) +
            ", planId=" + (plan != null ? plan.getId() : null) +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", status='" + status + '\'' +
            '}';
    }
}
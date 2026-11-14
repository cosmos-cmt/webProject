package com.cuda.gym.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(
    name = "class_bookings",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_class_bookings_user_class",
            columnNames = {"user_id", "class_id"}
        )
    },
    indexes = {
        @Index(name = "idx_bookings_user", columnList = "user_id"),
        @Index(name = "idx_bookings_class", columnList = "class_id"),
        @Index(name = "idx_bookings_booked_at", columnList = "booked_at")
    }
)
public class ClassBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_class_bookings_users")
    )
    @NotNull(message = "{classBooking.user.notNull}")
    private User user;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "class_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_class_bookings_classes")
    )
    @NotNull(message = "{classBooking.gymClass.notNull}")
    private GymClass gymClass;

    
    @Column(name = "booked_at", nullable = false)
    @NotNull(message = "{classBooking.bookedAt.notNull}")
    private LocalDateTime bookedAt;

    

    public ClassBooking() {
            }

    public ClassBooking(User user, GymClass gymClass, LocalDateTime bookedAt) {
        this.user = user;
        this.gymClass = gymClass;
        this.bookedAt = bookedAt;
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

    public GymClass getGymClass() {
        return gymClass;
    }

    
    public void setGymClass(GymClass gymClass) {
        this.gymClass = gymClass;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    
    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }

    // Domain helpers

   
    @Transient
    public String getDisplayLabel() {
        Long userId = (user != null ? user.getId() : null);
        Long classId = (gymClass != null ? gymClass.getId() : null);
        return "User #" + userId +
            " booked class #" + classId +
            " at " + bookedAt;
    }

  
    @Transient
    public boolean isForUser(User candidate) {
        if (candidate == null || user == null) {
            return false;
        }
        return Objects.equals(user.getId(), candidate.getId());
    }

    
    @Transient
    public boolean isForClass(GymClass candidate) {
        if (candidate == null || gymClass == null) {
            return false;
        }
        return Objects.equals(gymClass.getId(), candidate.getId());
    }

    

   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassBooking that)) return false;

        if (id != null && that.id != null) {
            return Objects.equals(id, that.id);
        }

        return Objects.equals(user, that.user) &&
               Objects.equals(gymClass, that.gymClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, gymClass);
    }

    

    @Override
    public String toString() {
        return "ClassBooking{" +
            "id=" + id +
            ", userId=" + (user != null ? user.getId() : null) +
            ", classId=" + (gymClass != null ? gymClass.getId() : null) +
            ", bookedAt=" + bookedAt +
            '}';
    }
}

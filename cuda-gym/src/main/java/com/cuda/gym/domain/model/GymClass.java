package com.cuda.gym.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(
    name = "classes",
    uniqueConstraints = {
        // Prevent duplicate class entries for the same trainer at the same day/time
        @UniqueConstraint(
            name = "uk_classes_trainer_day_time",
            columnNames = {"trainer_id", "day_of_week", "start_time"}
        )
    },
    indexes = {
        @Index(name = "idx_classes_trainer", columnList = "trainer_id"),
        @Index(name = "idx_classes_day", columnList = "day_of_week"),
        @Index(name = "idx_classes_difficulty", columnList = "difficulty")
    }
)
public class GymClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "trainer_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_classes_trainers")
    )
    @NotNull(message = "{gymClass.trainer.notNull}")
    private Trainer trainer;

    
    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "{gymClass.name.notBlank}")
    @Size(max = 100, message = "{gymClass.name.size}")
    private String name;

    
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    @NotNull(message = "{gymClass.dayOfWeek.notNull}")
    private DayOfWeek dayOfWeek;

        @Column(name = "start_time", nullable = false)
    @NotNull(message = "{gymClass.startTime.notNull}")
    private LocalTime startTime;

  
    @Column(name = "duration_minutes", nullable = false)
    @Min(value = 15, message = "{gymClass.durationMinutes.min}")
    @Max(value = 240, message = "{gymClass.durationMinutes.max}")
    private int durationMinutes;

        @Column(name = "difficulty", nullable = false, length = 20)
    @NotBlank(message = "{gymClass.difficulty.notBlank}")
    @Size(max = 20, message = "{gymClass.difficulty.size}")
    private String difficulty;

        @Column(name = "capacity", nullable = false)
    @Min(value = 1, message = "{gymClass.capacity.min}")
    @Max(value = 200, message = "{gymClass.capacity.max}")
    private int capacity;

      @OneToMany(
        mappedBy = "gymClass",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<ClassBooking> bookings = new HashSet<>();

   
    public GymClass() {
      
    }

    public GymClass(
        Trainer trainer,
        String name,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        int durationMinutes,
        String difficulty,
        int capacity
    ) {
        this.trainer = trainer;
        this.name = name;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.difficulty = difficulty;
        this.capacity = capacity;
    }

   

    public Long getId() {
        return id;
    }

        public void setId(Long id) {
        this.id = id;
    }

    public Trainer getTrainer() {
        return trainer;
    }
    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

        public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Set<ClassBooking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<ClassBooking> bookings) {
        this.bookings = bookings;
    }

    
    public void addBooking(ClassBooking booking) {
        bookings.add(booking);
        booking.setGymClass(this);
    }

    public void removeBooking(ClassBooking booking) {
        bookings.remove(booking);
        booking.setGymClass(null);
    }

    
    
    @Transient
    public int getCurrentBookingCount() {
        return bookings != null ? bookings.size() : 0;
    }

   
    @Transient
    public int getRemainingCapacity() {
        return Math.max(0, capacity - getCurrentBookingCount());
    }

    @Transient
    public boolean hasAvailableSlots() {
        return getRemainingCapacity() > 0;
    }

    @Transient
    public String getDisplayLabel() {
        String day = (dayOfWeek != null ? dayOfWeek.name() : "UNKNOWN_DAY");
        String time = (startTime != null ? startTime.toString() : "UNKNOWN_TIME");
        return name + " \u00b7 " + day + " " + time;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GymClass gymClass)) return false;

        if (id != null && gymClass.id != null) {
            return Objects.equals(id, gymClass.id);
        }

        return Objects.equals(trainer, gymClass.trainer) &&
               Objects.equals(dayOfWeek, gymClass.dayOfWeek) &&
               Objects.equals(startTime, gymClass.startTime) &&
               Objects.equals(name, gymClass.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainer, dayOfWeek, startTime, name);
    }

    @Override
    public String toString() {
        return "GymClass{" +
            "id=" + id +
            ", trainerId=" + (trainer != null ? trainer.getId() : null) +
            ", name='" + name + '\'' +
            ", dayOfWeek=" + dayOfWeek +
            ", startTime=" + startTime +
            ", durationMinutes=" + durationMinutes +
            ", difficulty='" + difficulty + '\'' +
            ", capacity=" + capacity +
            '}';
    }
}

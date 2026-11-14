package com.cuda.gym.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
    name = "plans",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_plans_name", columnNames = "name")
    }
)
public class Plan  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 60)
    @NotBlank(message = "{plan.name.notBlank}")
    @Size(max = 60, message = "{plan.name.size}")
    private String name;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "{plan.price.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{plan.price.min}")
    @Digits(integer = 8, fraction = 2, message = "{plan.price.digits}")
    private BigDecimal price;

    @Column(name = "duration_months", nullable = false)
    @Min(value = 1, message = "{plan.durationMonths.min}")
    @Max(value = 60, message = "{plan.durationMonths.max}")
    private int durationMonths;

    @Column(name = "description", nullable = false, length = 500)
    @NotBlank(message = "{plan.description.notBlank}")
    @Size(max = 500, message = "{plan.description.size}")
    private String description;

    @Column(name = "features", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "{plan.features.notBlank}")
    private String features;

    
    @OneToMany(
        mappedBy = "plan",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<Membership> memberships = new HashSet<>();

   
    public Plan() {
            }

    public Plan(
        String name,
        BigDecimal price,
        int durationMonths,
        String description,
        String features
    ) {
        this.name = name;
        this.price = price;
        this.durationMonths = durationMonths;
        this.description = description;
        this.features = features;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public Set<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<Membership> memberships) {
        this.memberships = memberships;
    }



    public void addMembership(Membership membership) {
        memberships.add(membership);
        membership.setPlan(this);
    }

    public void removeMembership(Membership membership) {
        memberships.remove(membership);
        membership.setPlan(null);
    }

    @Transient
    public String getDisplayName() {
        return name + " (" + durationMonths + " months)";
    }

    @Transient
    public boolean isLongTerm() {
        return durationMonths >= 12;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plan plan)) return false;
        if (id != null && plan.id != null) {
            return Objects.equals(id, plan.id);
        }
        return Objects.equals(name, plan.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

   
    @Override
    public String toString() {
        return "Plan{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", durationMonths=" + durationMonths +
            '}';
    }
}
package com.cuda.gym.domain.service.plan;

import com.cuda.gym.domain.model.Plan;
import com.cuda.gym.domain.repository.PlanRepository;
import com.cuda.gym.domain.service.plan.PlanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;

    public PlanServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    
    @Override
    @Transactional(readOnly = true)
    public Optional<Plan> findById(Long id) {
        return planRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Plan getByIdOrThrow(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Plan> findByName(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return planRepository.findByName(name.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean nameExists(String name) {
        if (name == null) {
            return false;
        }
        return planRepository.existsByName(name.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Plan> findAll(Pageable pageable) {
        return planRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plan> findAllOrderByPriceAsc() {
        return planRepository.findAllByOrderByPriceAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plan> findAllOrderByDurationAsc() {
        return planRepository.findAllByOrderByDurationMonthsAsc();
    }

    @Override
    public Plan createPlan(Plan plan) {
        if (plan == null) {
            throw new IllegalArgumentException("Plan must not be null");
        }

        String name = (plan.getName() != null) ? plan.getName().trim() : null;
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Plan name must not be blank");
        }

        if (nameExists(name)) {
            throw new IllegalArgumentException("Plan name already exists: " + name);
        }

        if (plan.getPrice() == null || plan.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Plan price must be positive");
        }

        if (plan.getDurationMonths() <= 0) {
            throw new IllegalArgumentException("Plan duration must be a positive number of months");
        }

        plan.setName(name);
        return planRepository.save(plan);
    }

    @Override
    public Plan updatePlan(Long planId, Plan updatedPlan) {
        if (updatedPlan == null) {
            throw new IllegalArgumentException("Updated plan must not be null");
        }

        Plan existing = getByIdOrThrow(planId);

                String newName = (updatedPlan.getName() != null) ? updatedPlan.getName().trim() : null;
        if (newName != null && !newName.isBlank() && !newName.equals(existing.getName())) {
            if (nameExists(newName)) {
                throw new IllegalArgumentException("Plan name already exists: " + newName);
            }
            existing.setName(newName);
        }

                if (updatedPlan.getPrice() != null) {
            if (updatedPlan.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Plan price must be positive");
            }
            existing.setPrice(updatedPlan.getPrice());
        }

                if (updatedPlan.getDurationMonths() > 0) {
            existing.setDurationMonths(updatedPlan.getDurationMonths());
        }

        
        if (updatedPlan.getDescription() != null && !updatedPlan.getDescription().isBlank()) {
            existing.setDescription(updatedPlan.getDescription().trim());
        }

        
        if (updatedPlan.getFeatures() != null && !updatedPlan.getFeatures().isBlank()) {
            existing.setFeatures(updatedPlan.getFeatures().trim());
        }

        return planRepository.save(existing);
    }

    @Override
    public void deletePlan(Long planId) {
        Plan existing = getByIdOrThrow(planId);
                planRepository.delete(existing);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<Plan> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAllOrderByPriceAsc();
        }
        return planRepository.findByNameContainingIgnoreCase(keyword.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plan> findByMaxPrice(BigDecimal maxPrice) {
        if (maxPrice == null || maxPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return findAllOrderByPriceAsc();
        }
        return planRepository.findByPriceLessThanEqual(maxPrice);
    }

  
    @Override
    @Transactional(readOnly = true)
    public long countAllPlans() {
        return planRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countLongTermPlans(int minMonths) {
        if (minMonths <= 0) {
            return planRepository.count();
        }
        return planRepository.countByDurationMonthsGreaterThanEqual(minMonths);
    }
}
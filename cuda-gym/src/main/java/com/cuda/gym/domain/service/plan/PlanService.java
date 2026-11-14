package com.cuda.gym.domain.service.plan;

import com.cuda.gym.domain.model.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PlanService {

      Optional<Plan> findById(Long id);

       Plan getByIdOrThrow(Long id);

        Optional<Plan> findByName(String name);

        boolean nameExists(String name);

       List<Plan> findAll();

        Page<Plan> findAll(Pageable pageable);

        List<Plan> findAllOrderByPriceAsc();

        List<Plan> findAllOrderByDurationAsc();

           Plan createPlan(Plan plan);

       Plan updatePlan(Long planId, Plan updatedPlan);

        void deletePlan(Long planId);

    
       List<Plan> searchByName(String keyword);

       List<Plan> findByMaxPrice(BigDecimal maxPrice);

    
    
    long countAllPlans();

        long countLongTermPlans(int minMonths);
}
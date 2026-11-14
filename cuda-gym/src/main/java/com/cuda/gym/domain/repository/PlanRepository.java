package com.cuda.gym.domain.repository;

import com.cuda.gym.domain.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

        Optional<Plan> findByName(String name);

       boolean existsByName(String name);

       List<Plan> findAllByOrderByPriceAsc();

        List<Plan> findAllByOrderByDurationMonthsAsc();

        List<Plan> findByNameContainingIgnoreCase(String keyword);

       List<Plan> findByPriceLessThanEqual(BigDecimal maxPrice);

        long countByDurationMonthsGreaterThanEqual(int minMonths);
}

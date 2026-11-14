package com.cuda.gym.domain.service.membership;

import com.cuda.gym.domain.model.Membership;
import com.cuda.gym.domain.model.Plan;
import com.cuda.gym.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MembershipService {

     
    Optional<Membership> findById(Long id);

       Membership getByIdOrThrow(Long id);

       List<Membership> findAll();

        Page<Membership> findAll(Pageable pageable);

       List<Membership> findByUser(User user);

        Page<Membership> findByUser(User user, Pageable pageable);

        Optional<Membership> findActiveMembership(User user);

        boolean hasActiveMembership(User user);

    
        Membership createMembership(User user, Plan plan, LocalDate startDate);

        Membership renewMembership(Long membershipId);

        Membership upgradeMembership(Long membershipId, Plan newPlan);

        Membership cancelMembership(Long membershipId);

    
    Membership expireMembership(Long membershipId);

    

    List<Membership> findByStatus(String status);

        Page<Membership> findByStatus(String status, Pageable pageable);

        List<Membership> findActiveWithinPeriod(LocalDate start, LocalDate end);

        List<Membership> findStartedBetween(LocalDate startDate, LocalDate endDate);

  
        long countByStatus(String status);

        long countByPlan(Plan plan);

        long countByPlanAndStatus(Plan plan, String status);
}
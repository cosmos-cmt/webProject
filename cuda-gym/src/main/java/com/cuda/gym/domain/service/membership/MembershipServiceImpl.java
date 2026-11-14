package com.cuda.gym.domain.service.membership;

import com.cuda.gym.domain.model.Membership;
import com.cuda.gym.domain.model.Plan;
import com.cuda.gym.domain.model.User;
import com.cuda.gym.domain.repository.MembershipRepository;
import com.cuda.gym.domain.repository.PlanRepository;
import com.cuda.gym.domain.service.membership.MembershipService;
import com.cuda.gym.domain.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;
    private final PlanRepository planRepository;
    private final UserService userService;

    public MembershipServiceImpl(
            MembershipRepository membershipRepository,
            PlanRepository planRepository,
            UserService userService
    ) {
        this.membershipRepository = membershipRepository;
        this.planRepository = planRepository;
        this.userService = userService;
    }

      @Override
    @Transactional(readOnly = true)
    public Optional<Membership> findById(Long id) {
        return membershipRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Membership getByIdOrThrow(Long id) {
        return membershipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Membership> findAll() {
        return membershipRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Membership> findAll(Pageable pageable) {
        return membershipRepository.findAll(pageable);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<Membership> findByUser(User user) {
        return membershipRepository.findByUserOrderByStartDateDesc(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Membership> findByUser(User user, Pageable pageable) {
        return membershipRepository.findByUserOrderByStartDateDesc(user, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Membership> findActiveMembership(User user) {
        return membershipRepository.findFirstByUserAndStatus(user, "ACTIVE");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasActiveMembership(User user) {
        return membershipRepository.existsByUserAndStatus(user, "ACTIVE");
    }

   
    @Override
    public Membership createMembership(User user, Plan plan, LocalDate startDate) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User must not be null and must have an ID");
        }
        if (plan == null || plan.getId() == null) {
            throw new IllegalArgumentException("Plan must not be null and must have an ID");
        }

                User managedUser = userService.getByIdOrThrow(user.getId());
        Plan managedPlan = planRepository.findById(plan.getId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + plan.getId()));

                if (hasActiveMembership(managedUser)) {
            throw new IllegalStateException("User already has an active membership");
        }

        LocalDate effectiveStart = (startDate != null) ? startDate : LocalDate.now();
        LocalDate endDate = effectiveStart.plusMonths(managedPlan.getDurationMonths());

        Membership membership = new Membership();
        membership.setUser(managedUser);
        membership.setPlan(managedPlan);
        membership.setStartDate(effectiveStart);
        membership.setEndDate(endDate);
        membership.setStatus("ACTIVE");

        return membershipRepository.save(membership);
    }

    @Override
    public Membership renewMembership(Long membershipId) {
        Membership membership = getByIdOrThrow(membershipId);

        if (!"ACTIVE".equalsIgnoreCase(membership.getStatus())) {
            throw new IllegalStateException("Only ACTIVE memberships can be renewed");
        }

        Plan plan = membership.getPlan();
        if (plan == null || plan.getDurationMonths() <= 0) {
            throw new IllegalStateException("Plan duration must be positive to renew membership");
        }

        LocalDate currentEnd = membership.getEndDate();
        if (currentEnd == null) {
            currentEnd = LocalDate.now();
        }

        LocalDate newEnd = currentEnd.plusMonths(plan.getDurationMonths());
        membership.setEndDate(newEnd);

        return membershipRepository.save(membership);
    }

    @Override
    public Membership upgradeMembership(Long membershipId, Plan newPlan) {
        Membership current = getByIdOrThrow(membershipId);

        if (newPlan == null || newPlan.getId() == null) {
            throw new IllegalArgumentException("New plan must not be null and must have an ID");
        }

        Plan managedNewPlan = planRepository.findById(newPlan.getId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + newPlan.getId()));

               current.setStatus("EXPIRED");
        membershipRepository.save(current);

                User user = current.getUser();
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusMonths(managedNewPlan.getDurationMonths());

        Membership upgraded = new Membership();
        upgraded.setUser(user);
        upgraded.setPlan(managedNewPlan);
        upgraded.setStartDate(start);
        upgraded.setEndDate(end);
        upgraded.setStatus("ACTIVE");

        return membershipRepository.save(upgraded);
    }

    @Override
    public Membership cancelMembership(Long membershipId) {
        Membership membership = getByIdOrThrow(membershipId);

        if ("CANCELLED".equalsIgnoreCase(membership.getStatus())) {
            return membership; // already cancelled
        }

        membership.setStatus("CANCELLED");
        
        return membershipRepository.save(membership);
    }

    @Override
    public Membership expireMembership(Long membershipId) {
        Membership membership = getByIdOrThrow(membershipId);

        membership.setStatus("EXPIRED");
                if (membership.getEndDate() != null && membership.getEndDate().isAfter(LocalDate.now())) {
            membership.setEndDate(LocalDate.now());
        }

        return membershipRepository.save(membership);
    }

      @Override
    @Transactional(readOnly = true)
    public List<Membership> findByStatus(String status) {
        return membershipRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Membership> findByStatus(String status, Pageable pageable) {
        return membershipRepository.findByStatus(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Membership> findActiveWithinPeriod(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end dates must not be null");
        }
        return membershipRepository
                .findByStartDateLessThanEqualAndEndDateGreaterThanEqual(end, start);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Membership> findStartedBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates must not be null");
        }
        return membershipRepository.findByStartDateBetween(startDate, endDate);
    }

        @Override
    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return membershipRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByPlan(Plan plan) {
        return membershipRepository.countByPlan(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByPlanAndStatus(Plan plan, String status) {
        return membershipRepository.countByPlanAndStatus(plan, status);
    }
}
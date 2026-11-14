package com.cuda.gym.web.viewmodel;

import com.cuda.gym.domain.model.Plan;
import com.cuda.gym.web.dto.MembershipForm;

import java.util.ArrayList;
import java.util.List;


public class MembershipViewModel {

        private List<Plan> plans = new ArrayList<>();

        private MembershipForm membershipForm;

    public MembershipViewModel() {
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public void setPlans(List<Plan> plans) {
        this.plans = plans != null ? plans : new ArrayList<>();
    }

    public MembershipForm getMembershipForm() {
        return membershipForm;
    }

    public void setMembershipForm(MembershipForm membershipForm) {
        this.membershipForm = membershipForm;
    }
}
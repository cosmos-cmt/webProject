package com.cuda.gym.web.mapper;

import com.cuda.gym.domain.model.Plan;
import com.cuda.gym.web.dto.PlanForm;

public class PlanMapper {

        public Plan toPlan(PlanForm form) {
        if (form == null) {
            return null;
        }
        Plan plan = new Plan();
               plan.setName(form.getName());
        plan.setPrice(form.getPrice());
        plan.setDurationMonths(form.getDurationMonths());
        plan.setDescription(form.getDescription());
        return plan;
    }

        public void updatePlanFromForm(Plan plan, PlanForm form) {
        if (plan == null || form == null) {
            return;
        }
        plan.setName(form.getName());
        plan.setPrice(form.getPrice());
        plan.setDurationMonths(form.getDurationMonths());
        plan.setDescription(form.getDescription());
    }

        public PlanForm toPlanForm(Plan plan) {
        if (plan == null) {
            return null;
        }
        PlanForm form = new PlanForm();
        form.setId(plan.getId());
        form.setName(plan.getName());
        form.setPrice(plan.getPrice());
        form.setDurationMonths(plan.getDurationMonths());
        form.setDescription(plan.getDescription());
        return form;
    }
}

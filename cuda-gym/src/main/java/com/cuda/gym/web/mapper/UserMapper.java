package com.cuda.gym.web.mapper;

import com.cuda.gym.domain.model.User;
import com.cuda.gym.web.dto.MemberEditForm;
import com.cuda.gym.web.dto.RegistrationForm;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

        public User toUser(RegistrationForm form) {
        if (form == null) {
            return null;
        }
        User user = new User();
        user.setFullName(form.getFullName());
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());
        user.setDateOfBirth(form.getDateOfBirth());
        user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        return user;
    }

        public void updateUserFromMemberEditForm(User user, MemberEditForm form) {
        if (user == null || form == null) {
            return;
        }
        user.setFullName(form.getFullName());
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());
        user.setDateOfBirth(form.getDateOfBirth());
    }

   
    public void updatePasswordIfRequested(User user, MemberEditForm form) {
        if (user == null || form == null) {
            return;
        }
        if (form.isPasswordChangeRequested() && form.isNewPasswordConfirmed()) {
            user.setPasswordHash(passwordEncoder.encode(form.getNewPassword()));
        }
    }

       public MemberEditForm toMemberEditForm(User user) {
        if (user == null) {
            return null;
        }
        MemberEditForm form = new MemberEditForm();
        form.setId(user.getId());
        form.setFullName(user.getFullName());
        form.setEmail(user.getEmail());
        form.setPhone(user.getPhone());
        form.setDateOfBirth(user.getDateOfBirth());
        
        return form;
    }
}
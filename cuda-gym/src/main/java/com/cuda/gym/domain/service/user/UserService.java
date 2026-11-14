package com.cuda.gym.domain.service.user;

import com.cuda.gym.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);

       User getByIdOrThrow(Long id);

        Optional<User> findByEmail(String email);

       boolean emailExists(String email);

        List<User> findAll();

        Page<User> findAll(Pageable pageable);

    
       User registerMember(User user, String rawPassword);

        User updateProfile(Long userId, String fullName, String phone);

        void changePassword(Long userId, String oldRawPassword, String newRawPassword);

           User updateAccountStatus(Long userId, String newStatus);

       User updateRole(Long userId, String newRole);

       void deactivateUser(Long userId);

   
       Page<User> searchByNameOrEmail(String keyword, Pageable pageable);

        Page<User> findByRoleAndStatus(String role, String status, Pageable pageable);

        long countByRole(String role);

        long countByAccountStatus(String status);
}
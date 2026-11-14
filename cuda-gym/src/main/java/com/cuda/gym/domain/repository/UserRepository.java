package com.cuda.gym.domain.repository;

import com.cuda.gym.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

        Optional<User> findByEmail(String email);

       boolean existsByEmail(String email);

      List<User> findByRole(String role);

        List<User> findByAccountStatus(String accountStatus);

        Page<User> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String fullNameKeyword,
            String emailKeyword,
            Pageable pageable
    );

       Page<User> findByRoleAndAccountStatus(
            String role,
            String accountStatus,
            Pageable pageable
    );

        long countByRole(String role);

       long countByAccountStatus(String accountStatus);
}
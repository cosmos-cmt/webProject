package com.cuda.gym.domain.service.user;

import com.cuda.gym.domain.model.User;
import com.cuda.gym.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    

    @Override
    public User registerMember(User user, String rawPassword) {
                if (user.getEmail() != null) {
            user.setEmail(user.getEmail().trim().toLowerCase());
        }

        if (emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use: " + user.getEmail());
        }

                String encoded = passwordEncoder.encode(rawPassword);
        user.setPasswordHash(encoded);

                if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("ROLE_MEMBER");
        }
        if (user.getAccountStatus() == null || user.getAccountStatus().isBlank()) {
            user.setAccountStatus("ACTIVE");
        }

        return userRepository.save(user);
    }

    @Override
    public User updateProfile(Long userId, String fullName, String phone) {
        User existing = getByIdOrThrow(userId);

        if (fullName != null && !fullName.isBlank()) {
            existing.setFullName(fullName.trim());
        }

        if (phone != null && !phone.isBlank()) {
            existing.setPhone(phone.trim());
        }


        return userRepository.save(existing);
    }

    @Override
    public void changePassword(Long userId, String oldRawPassword, String newRawPassword) {
        User user = getByIdOrThrow(userId);

        if (oldRawPassword != null && !oldRawPassword.isBlank()) {
            boolean matches = passwordEncoder.matches(oldRawPassword, user.getPasswordHash());
            if (!matches) {
                throw new IllegalArgumentException("Old password is incorrect");
            }
        }

        String encoded = passwordEncoder.encode(newRawPassword);
        user.setPasswordHash(encoded);
        userRepository.save(user);
    }

 
    @Override
    public User updateAccountStatus(Long userId, String newStatus) {
        User user = getByIdOrThrow(userId);
        user.setAccountStatus(newStatus);
        return userRepository.save(user);
    }

    @Override
    public User updateRole(Long userId, String newRole) {
        User user = getByIdOrThrow(userId);
        user.setRole(newRole);
        return userRepository.save(user);
    }

    @Override
    public void deactivateUser(Long userId) {
        User user = getByIdOrThrow(userId);
        user.setAccountStatus("BANNED");
        userRepository.save(user);
    }

       @Override
    @Transactional(readOnly = true)
    public Page<User> searchByNameOrEmail(String keyword, Pageable pageable) {
        String search = (keyword == null) ? "" : keyword.trim();
        return userRepository
                .findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findByRoleAndStatus(String role, String status, Pageable pageable) {
        return userRepository.findByRoleAndAccountStatus(role, status, pageable);
    }

      @Override
    @Transactional(readOnly = true)
    public long countByRole(String role) {
        return userRepository.countByRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByAccountStatus(String status) {
        return userRepository.countByAccountStatus(status);
    }
}
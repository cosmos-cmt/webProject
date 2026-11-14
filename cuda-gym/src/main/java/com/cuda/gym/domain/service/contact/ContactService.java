// src/main/java/com/cuda/gym/domain/service/ContactService.java
package com.cuda.gym.domain.service.contact;

import com.cuda.gym.domain.model.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ContactService {
    Optional<ContactMessage> findById(Long id);
    ContactMessage getByIdOrThrow(Long id);
    List<ContactMessage> findAll();
    Page<ContactMessage> findAll(Pageable pageable);
    ContactMessage submitMessage(ContactMessage message);
    ContactMessage markAsRead(Long messageId);
    ContactMessage archiveMessage(Long messageId);
    ContactMessage markAsNew(Long messageId);
    List<ContactMessage> findByStatus(String status);
    Page<ContactMessage> findByStatus(String status, Pageable pageable);
    Page<ContactMessage> searchBySubjectOrEmail(String keyword, Pageable pageable);
    long countByStatus(String status);
}
package com.cuda.gym.domain.repository;

import com.cuda.gym.domain.model.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

       List<ContactMessage> findByReadStatusOrderBySubmittedAtDesc(String readStatus);

       Page<ContactMessage> findByReadStatusOrderBySubmittedAtDesc(
            String readStatus,
            Pageable pageable
    );

    
    Page<ContactMessage> findBySubjectContainingIgnoreCaseOrderBySubmittedAtDesc(
            String subjectKeyword,
            Pageable pageable
    );

        Page<ContactMessage> findByEmailContainingIgnoreCaseOrderBySubmittedAtDesc(
            String emailKeyword,
            Pageable pageable
    );

       Page<ContactMessage> findBySubjectContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderBySubmittedAtDesc(
            String subjectKeyword,
            String emailKeyword,
            Pageable pageable
    );

        long countByReadStatus(String readStatus);
}
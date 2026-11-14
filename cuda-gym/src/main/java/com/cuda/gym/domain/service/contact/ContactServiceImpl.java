
package com.cuda.gym.domain.service.contact;

import com.cuda.gym.domain.model.ContactMessage;
import com.cuda.gym.domain.repository.ContactMessageRepository;
import com.cuda.gym.domain.service.contact.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {
    private final ContactMessageRepository contactMessageRepository;
    public ContactServiceImpl(ContactMessageRepository contactMessageRepository) { this.contactMessageRepository = contactMessageRepository; }
    @Override @Transactional(readOnly = true) public Optional<ContactMessage> findById(Long id) { return contactMessageRepository.findById(id); }
    @Override @Transactional(readOnly = true) public ContactMessage getByIdOrThrow(Long id) { return contactMessageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Contact message not found with id: " + id)); }
    @Override @Transactional(readOnly = true) public List<ContactMessage> findAll() { return contactMessageRepository.findAll(); }
    @Override @Transactional(readOnly = true) public Page<ContactMessage> findAll(Pageable pageable) { return contactMessageRepository.findAll(pageable); }
    @Override public ContactMessage submitMessage(ContactMessage message) {
        if (message == null) throw new IllegalArgumentException("ContactMessage must not be null");
        if (message.getName() != null) message.setName(message.getName().trim());
        if (message.getEmail() != null) message.setEmail(message.getEmail().trim());
        if (message.getSubject() != null) message.setSubject(message.getSubject().trim());
        if (message.getMessage() != null) message.setMessage(message.getMessage().trim());
        if (message.getSubmittedAt() == null) message.setSubmittedAt(LocalDateTime.now());
        if (message.getReadStatus() == null || message.getReadStatus().isBlank()) message.setReadStatus("NEW");
        return contactMessageRepository.save(message);
    }
    @Override public ContactMessage markAsRead(Long messageId) { ContactMessage message = getByIdOrThrow(messageId); message.setReadStatus("READ"); return contactMessageRepository.save(message); }
    @Override public ContactMessage archiveMessage(Long messageId) { ContactMessage message = getByIdOrThrow(messageId); message.setReadStatus("ARCHIVED"); return contactMessageRepository.save(message); }
    @Override public ContactMessage markAsNew(Long messageId) { ContactMessage message = getByIdOrThrow(messageId); message.setReadStatus("NEW"); return contactMessageRepository.save(message); }
    @Override @Transactional(readOnly = true) public List<ContactMessage> findByStatus(String status) { return contactMessageRepository.findByReadStatusOrderBySubmittedAtDesc(status); }
    @Override @Transactional(readOnly = true) public Page<ContactMessage> findByStatus(String status, Pageable pageable) { return contactMessageRepository.findByReadStatusOrderBySubmittedAtDesc(status, pageable); }
    @Override @Transactional(readOnly = true) public Page<ContactMessage> searchBySubjectOrEmail(String keyword, Pageable pageable) {
        String search = (keyword == null) ? "" : keyword.trim();
        return contactMessageRepository.findBySubjectContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderBySubmittedAtDesc(search, search, pageable);
    }
    @Override @Transactional(readOnly = true) public long countByStatus(String status) { return contactMessageRepository.countByReadStatus(status); }
}
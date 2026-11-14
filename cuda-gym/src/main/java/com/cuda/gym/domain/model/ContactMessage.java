package com.cuda.gym.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(
    name = "contact_messages",
    indexes = {
        @Index(name = "idx_contact_messages_status", columnList = "read_status"),
        @Index(name = "idx_contact_messages_submitted_at", columnList = "submitted_at")
    }
)
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "name", nullable = false, length = 120)
    @NotBlank(message = "{contactMessage.name.notBlank}")
    @Size(max = 120, message = "{contactMessage.name.size}")
    private String name;

    
    @Column(name = "email", nullable = false, length = 160)
    @NotBlank(message = "{contactMessage.email.notBlank}")
    @Email(message = "{contactMessage.email.valid}")
    @Size(max = 160, message = "{contactMessage.email.size}")
    private String email;

    
    @Column(name = "subject", nullable = false, length = 200)
    @NotBlank(message = "{contactMessage.subject.notBlank}")
    @Size(min = 5, max = 200, message = "{contactMessage.subject.size}")
    private String subject;

    
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "{contactMessage.message.notBlank}")
    @Size(min = 20, message = "{contactMessage.message.minSize}")
    private String message;

    
    @Column(name = "submitted_at", nullable = false)
    @NotNull(message = "{contactMessage.submittedAt.notNull}")
    private LocalDateTime submittedAt;

    
    @Column(name = "read_status", nullable = false, length = 20)
    @NotBlank(message = "{contactMessage.readStatus.notBlank}")
    @Size(max = 20, message = "{contactMessage.readStatus.size}")
    private String readStatus;

    
    public ContactMessage() {
        // Required by JPA
    }

    public ContactMessage(
        String name,
        String email,
        String subject,
        String message,
        LocalDateTime submittedAt,
        String readStatus
    ) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.submittedAt = submittedAt;
        this.readStatus = readStatus;
    }

   
    public Long getId() {
        return id;
    }

   
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

        public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

        public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getReadStatus() {
        return readStatus;
    }

       public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    

    @Transient
    public boolean isNew() {
        return "NEW".equalsIgnoreCase(readStatus);
    }

    @Transient
    public boolean isRead() {
        return "READ".equalsIgnoreCase(readStatus);
    }

    @Transient
    public boolean isArchived() {
        return "ARCHIVED".equalsIgnoreCase(readStatus);
    }

    
    @Transient
    public String getSummary() {
        String truncatedSubject = subject;
        if (subject != null && subject.length() > 50) {
            truncatedSubject = subject.substring(0, 47) + "...";
        }
        return "[" + readStatus + "] " + truncatedSubject + " from " + name;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactMessage that)) return false;

        if (id != null && that.id != null) {
            return Objects.equals(id, that.id);
        }

        return Objects.equals(email, that.email) &&
               Objects.equals(submittedAt, that.submittedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, submittedAt);
    }

  

    @Override
    public String toString() {
        String truncatedMessage = message;
        if (truncatedMessage != null && truncatedMessage.length() > 60) {
            truncatedMessage = truncatedMessage.substring(0, 57) + "...";
        }

        return "ContactMessage{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", subject='" + subject + '\'' +
            ", submittedAt=" + submittedAt +
            ", readStatus='" + readStatus + '\'' +
            ", message='" + truncatedMessage + '\'' +
            '}';
    }
}

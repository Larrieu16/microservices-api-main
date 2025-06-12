package com.coelho.notification_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tutorEmail;
    private String subject;
    private String message;
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
    private String status;
    private String petName;

    public String getSentAt() {
        return sentAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}

package com.coelho.notification_service.controller;

import com.coelho.notification_service.model.Notification;
import com.coelho.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PutMapping("/mark-as-read")
    public ResponseEntity<Void> markAsRead(@RequestBody List<Long> ids) {
        notificationService.markAsRead(ids);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }
}
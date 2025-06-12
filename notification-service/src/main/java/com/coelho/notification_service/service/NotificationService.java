package com.coelho.notification_service.service;

import com.coelho.notification_service.config.RabbitConfig;
import com.coelho.notification_service.model.Notification;
import com.coelho.notification_service.repository.NotificationRepository;
import com.coelho.pet_agenda_service.model.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    private final RetryTemplate retryTemplate;
    private final NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @RabbitListener(queues = "appointment-created-queue")
    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void handleAppointmentCreatedEvent(Appointment appointment) {
        retryTemplate.execute(context -> {
            try {
                sendEmailToTutor(appointment);

                // 🔴 Salvar a notificação no banco de dados
                Notification notification = new Notification();
                notification.setPetName(appointment.getPetName());
                notification.setTutorEmail(appointment.getTutorEmail());
                notification.setSubject("Agendamento de cuidados para " + appointment.getPetName());
                notification.setSentAt(LocalDateTime.now());
                notification.setStatus("Enviado");

                notificationRepository.save(notification);

                return null;
            } catch (Exception e) {
                throw new AmqpRejectAndDontRequeueException("Erro ao enviar e-mail", e);
            }
        });
    }

    private void sendEmailToTutor(Appointment appointment) {
        String subject = "Agendamento de cuidados para o seu pet: " + appointment.getPetName();

        String message = String.format(
                "Olá %s,\n\n" +
                        "Gostaríamos de informar que seu pet %s foi agendado para o cuidado: %s.\n" +
                        "📅 Data do agendamento: %s.\n\n" +
                        "Status atual do agendamento: %s.\n\n" +
                        "Nosso horário de funcionamento é por ordem de chegada, de 08:00 até 18:00.\n\n" +
                        "Agradecemos pela confiança em nossos serviços!\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe StarterPet",
                appointment.getTutorName(),
                appointment.getPetName(),
                appointment.getCareType(),
                appointment.getDays().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                appointment.getStatus()
        );

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(appointment.getTutorEmail());
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }

    public void markAsRead(List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);
        for (Notification notification : notifications) {
            notification.setStatus("Lido");
        }
        notificationRepository.saveAll(notifications);
    }
}
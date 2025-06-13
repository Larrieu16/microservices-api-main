package com.coelho.pet_agenda_service.service;

import com.coelho.pet_agenda_service.config.RabbitConfig;
import com.coelho.pet_agenda_service.model.Appointment;
import com.coelho.pet_agenda_service.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.coelho.pet_agenda_service.dto.PetDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void handlePetCreatedEvent(PetDTO pet) {
        List<Appointment> appointments = new ArrayList<>();

        if (pet.getAge() < 6) {
            appointments.add(createAppointment(
                    pet.getId(), "Vacinação Inicial", 7, pet.getTutorEmail(),
                    pet.getName(), pet.getTutorName(), true));
            appointments.add(createAppointment(
                    pet.getId(), "Check-up Inicial", 7, pet.getTutorEmail(),
                    pet.getName(), pet.getTutorName(), true));
        } else {
            appointments.add(createAppointment(
                    pet.getId(), "Check-up Inicial", 7, pet.getTutorEmail(),
                    pet.getName(), pet.getTutorName(), true));
        }
        appointments.add(createAppointment(
                pet.getId(), "Primeiro Banho Grátis", 15, pet.getTutorEmail(),
                pet.getName(), pet.getTutorName(), true));

        if (!pet.isNeutered()) {
            int castrationDays = pet.getSpecies().equalsIgnoreCase("cat") ? 180 : 270;
            appointments.add(createAppointment(
                    pet.getId(), "Castração", castrationDays, pet.getTutorEmail(),
                    pet.getName(), pet.getTutorName(), true));
        }

        appointmentRepository.saveAll(appointments);
        appointments.forEach(a -> rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME, "appointment.created", a));
    }

    public Appointment createAppointment(
            Long petId, String type, int days, String tutorEmail, String petName, String tutorName, boolean automatic) {

        Appointment appointment = new Appointment();
        appointment.setPetId(petId);
        appointment.setCareType(type);
        appointment.setDays(LocalDate.now().plusDays(days));
        appointment.setStatus("Pendente");
        appointment.setTutorEmail(tutorEmail);
        appointment.setPetName(petName);
        appointment.setTutorName(tutorName);
        appointment.setAutomatic(automatic);

        return appointmentRepository.save(appointment);
    }

    public Appointment createManualAppointment(Appointment appointment) {
        appointment.setAutomatic(false);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        publishAppointmentCreatedEvent(savedAppointment);
        return savedAppointment;
    }

    public void publishAppointmentCreatedEvent(Appointment appointment) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,
                "appointment.created", appointment);

    }

    public Appointment updateAppointmentStatus(Long id, String status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));

        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public Appointment rescheduleAppointment(Long id, int days) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));

        appointment.setDays(appointment.getDays().plusDays(days)); 
        appointment.setStatus("Reagendado");
        return appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Agendamento não encontrado!");
        }
        appointmentRepository.deleteById(id);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}


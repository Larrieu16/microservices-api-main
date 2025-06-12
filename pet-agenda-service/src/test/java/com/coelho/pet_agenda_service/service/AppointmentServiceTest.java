package com.coelho.pet_agenda_service.service;

import com.coelho.pet_agenda_service.model.Appointment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;


@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void shouldCreateAppointmentCorrectly() {
        Long petId = 1L;
        String careType = "Vacinação";
        int days = 15;
        String tutorEmail = "tutor@email.com";
        String petName = "Douradinho";
        String tutorName = "Luisa Larrieu";

        Appointment appointment = appointmentService.createAppointment(petId, careType, days, tutorEmail,
                petName, tutorName);

        Assertions.assertNotNull(appointment);
        Assertions.assertEquals(petId, appointment.getPetId());
        Assertions.assertEquals(careType, appointment.getCareType());
        Assertions.assertEquals(LocalDate.now().plusDays(days), appointment.getDays());
        Assertions.assertEquals("Pendente", appointment.getStatus());
        Assertions.assertEquals(tutorEmail, appointment.getTutorEmail());
        Assertions.assertEquals(petName, appointment.getPetName());
        Assertions.assertEquals(tutorName, appointment.getTutorName());
    }
}

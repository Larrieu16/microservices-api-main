package com.coelho.pet_agenda_service.repository;

import com.coelho.pet_agenda_service.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPetId(Long petId);
}

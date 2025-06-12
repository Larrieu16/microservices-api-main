package com.coelho.pet_agenda_service.controller;

import com.coelho.pet_agenda_service.dto.AppointmentRequestDTO;
import com.coelho.pet_agenda_service.model.Appointment;
import com.coelho.pet_agenda_service.service.AppointmentService;
import com.coelho.pet_register_service.controller.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pets/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Operation(
            summary = "Cria um agendamento manual",
            description = "Permite criar um agendamento manual informando o ID do pet, tipo de cuidado, dias até a realização e e-mail do tutor.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",
                            description = "Agendamento criado com sucesso."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                            description = "Requisição inválida."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",
                            description = "Erro interno no servidor.")
            }
    )
    @PostMapping("/manual")
    public ResponseEntity<ApiResponse> createManualAppointment(
            @RequestBody AppointmentRequestDTO request) {

        Appointment appointment = appointmentService.createAppointment(
                request.getPetId(),
                request.getCareType(),
                request.getDays(),
                request.getTutorEmail(),
                request.getPetName(),
                request.getTutorName(),
                false
        );

        appointmentService.publishAppointmentCreatedEvent(appointment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Agendamento manual criado com sucesso!", appointment));
    }

    @Operation(
            summary = "Lista todos os agendamentos de cuidados para os pets",
            description = "Este endpoint retorna todos os agendamentos criados, tanto manuais quanto automáticos.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                            description = "Lista de agendamentos"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",
                            description = "Erro interno no servidor.")
            }
    )
    @GetMapping("/list")
    public ResponseEntity<List<Appointment>> listAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/confirm/{id}")
    public ResponseEntity<ApiResponse> confirmAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentService.updateAppointmentStatus(id, "Confirmado");
        return ResponseEntity.ok(new ApiResponse("Agendamento confirmado com sucesso!", appointment));
    }

    @PutMapping("/reschedule/{id}/{days}")
    public ResponseEntity<ApiResponse> rescheduleAppointment(@PathVariable Long id, @PathVariable int days) {
        Appointment appointment = appointmentService.rescheduleAppointment(id, days);
        return ResponseEntity.ok(new ApiResponse("Agendamento reagendado com sucesso!", appointment));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(new ApiResponse("Agendamento excluído com sucesso!", null));
    }

}
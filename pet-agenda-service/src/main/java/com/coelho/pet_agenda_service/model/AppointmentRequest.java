package com.coelho.pet_agenda_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representa um agendamento de cuidado para um pet.")
public class AppointmentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Identificador do pet relacionado ao agendamento.",
            example = "10")
    private Long petId;

    @Schema(description = "Tipo de cuidado agendado.",
            example = "Vacinação")
    private String careType;

    @Schema(description = "Data do agendamento.",
            example = "2025-03-15")
    private int days;

    @Schema(description = "Status atual do agendamento.",
            example = "Pendente")
    private String status;

    @Schema(description = "E-mail do tutor para notificação.",
            example = "tutor@email.com")
    private String tutorEmail;
}

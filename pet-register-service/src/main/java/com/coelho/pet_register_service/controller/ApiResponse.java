package com.coelho.pet_register_service.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Resposta padrão da API contendo mensagem e dados.")
public class ApiResponse {
    @Schema(description = "Mensagem de status da operação",
            example = "Pet cadastrado com sucesso!")
    private String message;
    @Schema(description = "Dados retornados pela requisição")
    private Object data;

}
package com.coelho.pet_register_service.exception;


import com.coelho.pet_register_service.controller.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException{

    @ExceptionHandler(BreedNotFoundException.class)
    public ResponseEntity<ApiResponse> handleRacaNotFound(BreedNotFoundException ex) {
        ApiResponse response = new ApiResponse("Erro: " +
                ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCachorroNotFound(PetNotFoundException ex) {
        ApiResponse response = new ApiResponse("Erro: " +
                ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleNotFound(RuntimeException ex) {
        ApiResponse response = new ApiResponse("Erro: " +
                ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(org.springframework.web.client.RestClientException.class)
    public ResponseEntity<ApiResponse> handleExternalApiOut(Exception ex) {
        ApiResponse response = new ApiResponse("Parece que a API externa TheDogAPI " +
                "se encontra indisponível no momento: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(IllegalArgumentException ex) {
        ApiResponse response = new ApiResponse("Requisição inválida: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        ApiResponse response = new ApiResponse("Um erro inesperado acabou de " +
                "acontecer, tente novamente!." + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

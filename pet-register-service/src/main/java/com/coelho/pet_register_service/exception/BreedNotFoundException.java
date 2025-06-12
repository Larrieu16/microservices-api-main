package com.coelho.pet_register_service.exception;

public class BreedNotFoundException extends RuntimeException {
    public BreedNotFoundException(String message) {
        super(message);
    }
}

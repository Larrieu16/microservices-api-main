package com.coelho.pet_register_service.exception;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException(String message) {
        super(message);
    }
}

package com.coelho.pet_register_service.repository;

import com.coelho.pet_register_service.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByBreed(String breed);
    List<Pet> findBySpecies(String species);
}

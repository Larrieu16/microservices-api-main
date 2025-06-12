package com.coelho.pet_register_service.service;

import com.coelho.pet_register_service.model.Pet;
import com.coelho.pet_register_service.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @Test
    void shouldReturnListOfPets() {
        List<Pet> pets = List.of(new Pet(1L, "Douradinho",
                "Labrador Retriever", "dog",
                "Luisa Larrieu", "larrieu@email.com",
                2, 10.5,
                "Loiro", "Informação vinda da API",
                "Informação vinda da API", "Informação vinda da API"));

        Mockito.when(petRepository.findAll()).thenReturn(pets);

        List<Pet> result = petService.listAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Douradinho", result.get(0).getName());
    }
}


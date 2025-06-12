package com.coelho.pet_register_service.service;


import com.coelho.pet_register_service.dto.PetDTO;
import com.coelho.pet_register_service.exception.BreedNotFoundException;
import com.coelho.pet_register_service.exception.PetNotFoundException;
import com.coelho.pet_register_service.model.Pet;
import com.coelho.pet_register_service.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository repository;
    private final PetApiService petApiService;
    private final RabbitTemplate rabbitTemplate;

    public List<Pet> listAll() {
        return repository.findAll();
    }
    public List<Pet> listByBreed(String breed) {
        return repository.findByBreed(breed);
    }
    public List<Pet> listCats() {
        return repository.findBySpecies("cat");
    }
    public List<Pet> listDogs() {
        return repository.findBySpecies("dog");
    }

    public Pet findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet com ID " + id + " não encontrado."));
    }

    public List<Pet> findByBreedWithImage(String breed) {
        List<Pet> pets = repository.findByBreed(breed);
        if (pets.isEmpty()) {
            throw new BreedNotFoundException("A raça procurada não existe.");
        }

        pets.forEach(pet -> {
            List<Map<String, Object>> images = petApiService.findImageByBreed(pet.getSpecies(), pet.getBreed());
            if (!images.isEmpty()) {
                pet.setUrlImage((String) images.get(0).get("url"));
            }
        });

        return pets;
    }

    public Pet save(Pet pet) {
        if (pet.getSpecies() != null && pet.getBreed() != null) {
            List<Map<String, Object>> images = petApiService.findImageByBreed(pet.getSpecies(), pet.getBreed());
            if (!images.isEmpty()) {
                pet.setUrlImage((String) images.get(0).get("url"));
            }

            List<Map<String, Object>> breeds = petApiService.listBreeds(pet.getSpecies());
            Optional<Map<String, Object>> breed = breeds.stream()
                    .filter(r -> pet.getBreed().equalsIgnoreCase((String) r.get("name")))
                    .findFirst();

            breed.ifPresent(r -> {
                pet.setDescription((String) r.getOrDefault("description", "Descrição não disponível"));
                pet.setTemperament((String) r.getOrDefault("temperament", "Temperamento não disponível"));
            });
        }

        Pet savedPet = repository.save(pet);

        PetDTO petDTO = new PetDTO(
                savedPet.getId(), savedPet.getName(), savedPet.getBreed(),
                savedPet.getSpecies(), savedPet.getTutorName(), savedPet.getTutorEmail(),
                savedPet.getAge(), savedPet.getWeight(), savedPet.getColor(), savedPet.getDescription(),
                savedPet.getUrlImage(), savedPet.getTemperament());

        rabbitTemplate.convertAndSend("pet_exchange", "pet.created", petDTO);
        return savedPet;
    }

    public Pet update(Long id, Pet petUpdate) {
        return repository.findById(id).map(pet -> {
            pet.setName(petUpdate.getName());
            pet.setBreed(petUpdate.getBreed());
            pet.setSpecies(petUpdate.getSpecies());
            pet.setAge(petUpdate.getAge());
            pet.setWeight(petUpdate.getWeight());
            pet.setColor(petUpdate.getColor());
            pet.setTutorName(petUpdate.getTutorName());
            pet.setTutorEmail(petUpdate.getTutorEmail());

            if (!pet.getSpecies().equalsIgnoreCase(petUpdate.getSpecies()) ||
                    !pet.getBreed().equalsIgnoreCase(petUpdate.getBreed())) {

                List<Map<String, Object>> images = petApiService.findImageByBreed(petUpdate.getSpecies(), petUpdate.getBreed());
                if (!images.isEmpty()) {
                    pet.setUrlImage((String) images.get(0).get("url"));
                }

                List<Map<String, Object>> breeds = petApiService.listBreeds(petUpdate.getSpecies());
                Optional<Map<String, Object>> breed = breeds.stream()
                        .filter(r -> petUpdate.getBreed().equalsIgnoreCase((String) r.get("name")))
                        .findFirst();

                breed.ifPresent(r -> {
                    pet.setDescription((String) r.getOrDefault("description", "Descrição não disponível"));
                    pet.setTemperament((String) r.getOrDefault("temperament", "Temperamento não disponível"));
                });
            }

            return repository.save(pet);
        }).orElseThrow(() -> new PetNotFoundException("Pet não encontrado na base de dados."));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new PetNotFoundException("Pet com ID " + id + " não existe para ser deletado.");
        }
        repository.deleteById(id);
    }
}


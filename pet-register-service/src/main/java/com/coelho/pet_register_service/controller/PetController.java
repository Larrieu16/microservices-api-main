package com.coelho.pet_register_service.controller;

import com.coelho.pet_register_service.model.Pet;
import com.coelho.pet_register_service.service.PetApiService;
import com.coelho.pet_register_service.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@Tag(name = "Pets", description = "Endpoints para registro de pets")
public class PetController {
    private final PetService petService;
    private final PetApiService petApiService;

    @Operation(summary = "Cadastrar um novo pet")
    @PostMapping
    public ResponseEntity<ApiResponse> save(@RequestBody @Valid Pet pet) {
        Pet savedPet = petService.save(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Pet cadastrado com sucesso!", savedPet));
    }

    @Operation(summary = "Deletar um pet pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.ok(new ApiResponse("Pet deletado com sucesso!", null));
    }

    @Operation(summary = "Atualizar informações de um pet")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody @Valid Pet pet) {
        return ResponseEntity.ok(new ApiResponse("Pet atualizado com sucesso!",
                petService.update(id, pet)));
    }

    @Operation(summary = "Buscar pet por ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Pet encontrado com sucesso!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = "Pet não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse("Pet encontrado!",
                petService.findById(id)));
    }

    @Operation(summary = "Listar todos os pets")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Lista de pets retornada com sucesso!")
    })
    @GetMapping
    public ResponseEntity<ApiResponse> listAll() {
        return ResponseEntity.ok(new ApiResponse("Lista de pets retornada com sucesso!",
                petService.listAll()));
    }

    @Operation(summary = "Busca pets pela raça")
    @GetMapping("/breed")
    public ResponseEntity<ApiResponse> findByBreed(
            @Parameter(description = "Raça do pet")@RequestParam String breed) {
        return ResponseEntity.ok(new ApiResponse("Raça encontrada: " + breed,
                petService.findByBreedWithImage(breed)));
    }

    @Operation(summary = "Listar todos os cães cadastrados")
    @GetMapping("/dogs")
    public List<Pet> getDogs() {
        return petService.listDogs();
    }

    @Operation(summary = "Listar todos os gatos cadastrados")
    @GetMapping("/cats")
    public List<Pet> getCats() {
        return petService.listCats();
    }

    @Operation(summary = "Listar raças através da API externa TheDogAPI ou TheCatAPI")
    @GetMapping("/breeds")
    public List<Map<String, Object>> listBreeds(
    @Parameter(description = "Espécie do pet", example = "dog")
    @RequestParam String species) {
        return petApiService.listBreeds(species);
    }

    @Operation(summary = "Listar imagens da raça através da API externa TheDogAPI ou TheCatAPI")
    @GetMapping("/images")
    public List<Map<String, Object>> findImages(
            @Parameter(description = "Espécie do pet", example = "cat")
            @RequestParam String species,
            @Parameter(description = "Raça do pet", example = "siamese")
            @RequestParam String breed) {
        return petApiService.findImageByBreed(species, breed);
    }
}


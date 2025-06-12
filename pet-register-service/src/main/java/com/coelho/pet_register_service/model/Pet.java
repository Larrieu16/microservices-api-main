package com.coelho.pet_register_service.model;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representação de um pet cadastrado no sistema.")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do pet", example = "1")
    private Long id;

    @Schema(description = "Nome do pet", example = "Douradinho")
    private String name;

    @Schema(description = "Raça do pet", example = "Golden Retriever")
    private String breed;

    @Schema(description = "Espécie do pet", example = "dog")
    private String species;

    @Schema(description = "Nome do tutor do pet", example = "Luisa Larrieu")
    private String tutorName;

    @Schema(description = "E-mail do tutor do pet", example = "luisa.larrieu@email.com")
    private String tutorEmail;

    @Schema(description = "Idade do pet em *meses*", example = "3")
    private int age;

    @Schema(description = "Peso do pet em quilogramas", example = "12.5")
    private double weight;

    @Schema(description = "Cor predominante do pet", example = "Loiro")
    private String color;

    @Schema(description = "Descrição fornecida pela API, se disponível",
            example = "TheDogAPI ou TheCatAPI")
    private String description;

    @Schema(description = "Descrição fornecida pela API, se disponível",
            example = "TheDogAPI ou TheCatAPI")
    private String urlImage;

    @Schema(description = "Descrição fornecida pela API, se disponível",
            example = "TheDogAPI ou TheCatAPI")
    private String temperament;
}
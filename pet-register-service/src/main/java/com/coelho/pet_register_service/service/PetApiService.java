package com.coelho.pet_register_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PetApiService {
    @Value("${thedogapi.apikey}")
    private String dogApiKey;
    @Value("${thecatapi.apikey}")
    private String catApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders createHeaders(String species) {
        HttpHeaders headers = new HttpHeaders();
        String apiKey = species.equalsIgnoreCase("dog") ? dogApiKey : catApiKey;
        headers.set("x-api-key", apiKey);
        return headers;
    }

    public List<Map<String, Object>> listBreeds(String species) {
        String url = species.equalsIgnoreCase("dog")
                ? "https://api.thedogapi.com/v1/breeds"
                : "https://api.thecatapi.com/v1/breeds";

        HttpEntity<String> entity = new HttpEntity<>(createHeaders(species));
        ResponseEntity<Map[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map[].class);

        return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
    }

    public List<Map<String, Object>> findImageByBreed(String species, String breed) {
        List<Map<String, Object>> breeds = listBreeds(species);
        Optional<Map<String, Object>> foundBreed = breeds.stream()
                .filter(r -> breed.equalsIgnoreCase((String) r.getOrDefault("name", "")))
                .findFirst();

        return (List<Map<String, Object>>) foundBreed.map(r -> {
            String breedId = String.valueOf(r.get("id"));
            String url = (species.equalsIgnoreCase("dog")
                    ? "https://api.thedogapi.com/v1/images/search?breed_id="
                    : "https://api.thecatapi.com/v1/images/search?breed_id=") + breedId + "&limit=1";
            HttpEntity<String> entity = new HttpEntity<>(createHeaders(species));
            ResponseEntity<Map[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map[].class);
            return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
        }).orElse(Collections.emptyList());
    }
}
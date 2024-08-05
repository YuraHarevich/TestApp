package com.example.TestApp.service;

import com.example.TestApp.DTO.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@Service
public class LoadDataService {
    private final RestTemplate restTemplate;
    private final ElasticService service;
    private final String url = System.getenv("DATA_URL");
    private final String token = System.getenv("DATA_TOKEN");
    private static final Logger logger = LoggerFactory.getLogger(ElasticService.class);
    private final ObjectMapper objectMapper;



    public LoadDataService(RestTemplate restTemplate, ElasticService service, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.service = service;
        this.objectMapper = objectMapper;
    }

    public void fetchDataAndSave() {
        try {
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token);
            ProductDTO[] products = restTemplate.
                    exchange(url,
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            ProductDTO[].class)
                    .getBody();

            if (products != null) {
                for (ProductDTO product : products) {
                    service.save(product.toProduct());
                }
            }
        }catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.error("Resource not found at URL: {}. Status code: 404", url);
                loadFallbackProducts();
            } else {
                logger.error("HTTP error while fetching products: {}", e.getMessage());
            }
        }


    }

    private void loadFallbackProducts() {
        try {
            // Загрузка продуктов из файла
            ProductDTO[] fallbackProducts = objectMapper.readValue(new File("/app/backup.json"), ProductDTO[].class);
            for (ProductDTO product : fallbackProducts) {
                service.save(product.toProduct());
            }
        } catch (IOException e) {
            logger.error("Failed to load fallback products from file: {}", e.getMessage());
        }
    }
}
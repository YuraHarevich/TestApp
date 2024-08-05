package com.example.TestApp.service;

import com.example.TestApp.DTO.ProductDTO;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoadDataService {
    private final RestTemplate restTemplate;
    private final ElasticService service;
//    private final String url = System.getenv("DATA_URL");
//    private final String token = System.getenv("DATA_TOKEN");
    private final String url = "https://api.json-generator.com/templates/Vrhn7xe8c7z9/data";
    private final String token = "zax29jvzjrda6tf49j4o4ki460zapbkcf4tmyijx";
    private static final Logger logger = LoggerFactory.getLogger(ElasticService.class);


    public LoadDataService(RestTemplate restTemplate, ElasticService service) {
        this.restTemplate = restTemplate;
        this.service = service;
    }

    public void fetchDataAndSave() {
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
    }
}
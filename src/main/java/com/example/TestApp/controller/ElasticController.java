package com.example.TestApp.controller;

import com.example.TestApp.DTO.ProductDTO;
import com.example.TestApp.exception.GetProductException;
import com.example.TestApp.exception.SaveException;
import com.example.TestApp.response.MyErrorResponse;
import com.example.TestApp.service.ElasticService;
import com.example.TestApp.service.LoadDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("elastic")
public class ElasticController {
    private final ElasticService service;
    private final LoadDataService loadDataService;

    public ElasticController(ElasticService service, LoadDataService loadDataService) {
        this.service = service;
        this.loadDataService = loadDataService;
    }

    @PostMapping("/save")
    public void save(@RequestBody ProductDTO productDTO){
        service.save(productDTO.toProduct());
    }
    @GetMapping("/get")
    public List<ProductDTO> get(){
        return service.getAll();
    }

    @PostMapping("/load")
    public void load(){
       loadDataService.fetchDataAndSave();
    }
//    @GetMapping("/search")
//    public List<ProductDTO> searchByName(@RequestParam("name") String name){
//        return service.searchByName(name);
//    }
    @GetMapping("/search")
    public List<ProductDTO> searchByNameAndCountry(@RequestParam("name") String name,
                                                   @RequestParam("country") String country){
        return service.searchByNameAndCountry(name,country);
    }

    //~~~~~~~~~~~~~EXCEPTION HANDLING~~~~~~~~~~~~~~
    @ExceptionHandler
    ResponseEntity<MyErrorResponse> saveException(SaveException e){
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return new ResponseEntity<>(
                new MyErrorResponse(e.getMessage(),
                        now.format(formatter)),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler
    ResponseEntity<MyErrorResponse> getProductException(GetProductException e){
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return new ResponseEntity<>(
                new MyErrorResponse(
                        e.getMessage(),
                        now.format(formatter)),
                HttpStatus.BAD_REQUEST);
    }
}

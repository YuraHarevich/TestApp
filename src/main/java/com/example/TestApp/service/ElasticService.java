package com.example.TestApp.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.example.TestApp.DTO.ProductDTO;
import com.example.TestApp.entity.Product;
import com.example.TestApp.entity.Sku;
import com.example.TestApp.exception.GetProductException;
import com.example.TestApp.exception.SaveException;
import com.example.TestApp.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ElasticService {
    private final int searchSize = 1000;
    private final ProductRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(ElasticService.class);
    private final ElasticsearchClient esClient;

    public ElasticService(ProductRepository repository, ElasticsearchClient esClient) {
        this.repository = repository;
        this.esClient = esClient;
    }
    @Transactional
    public void save(Product product){
        if(product.getSkus()==null) {
            logger.error("Exception while saving product with id {}, product.sku == null", product.getId());
            throw new SaveException("Cant save product cause product.sku==null");
        }
        for(Sku sku: product.getSkus()){
            sku.setProduct(product);
        }
        repository.save(product);

        //////////////esClient saving/////////////////////
        ProductDTO productDTO = repository.findFirstByNameOrderByIdDesc(product.getName()).toProductDTO();

        try {
            IndexResponse response = esClient.index(i -> i
                    .index("products")
                    .id(String.valueOf(productDTO.getId()))
                    .document(productDTO)
            );
        } catch (IOException e) {
            logger.error("error while trying to save elements in elastic");
            throw new SaveException("Elastic client cant save product with id = "+ productDTO.getId());
        }
    }
    public List<ProductDTO> getAll(){
        try {
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("products")
                    .query(q -> q
                            .matchAll(m -> m)
                    )
                    .size(searchSize)
            );
            SearchResponse<ProductDTO> searchResponse = esClient.search(searchRequest, ProductDTO.class);
            return searchResponse.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("error while trying to get elements from elastic");
            throw new GetProductException("Exception while trying to get elements from elastic");
        } catch(ElasticsearchException e){
            logger.error("no such index");
            throw new GetProductException("no such index");
        }

    }

    public List<ProductDTO> searchByNameAndCountry(String name, String countryOfProduction){
        try {
            Query byName = MatchQuery.of(m -> m
                    .field("name")
                    .query(name)
            )._toQuery();

// Search by max price
            Query byCountry = MatchQuery.of(m -> m
                    .field("countryOfProduction")
                    .query(countryOfProduction)
            )._toQuery();

// Combine name and price queries to search the product index
            SearchResponse<ProductDTO> response = esClient.search(s -> s
                            .index("products")
                            .query(q -> q
                                    .bool(b -> b
                                            .must(byName)
                                            .must(byCountry)
                                    )
                            ),
                    ProductDTO.class

            );
            return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean empty() {
        return repository.count()==0;
    }
}

package com.shoponline.elastic.service;

import com.shoponline.elastic.entity.Inventory;
import com.shoponline.elastic.entity.InventoryEs;
import com.shoponline.elastic.repository.InventoryEsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class InventoryEsService {

    private InventoryEsRepository repository;

    private RestHighLevelClient restHighLevelClient;

    private final String INDEX_NAME = "inventory";

    public InventoryEs create(Inventory inventory) {
        InventoryEs inventoryEs = new InventoryEs();
        inventoryEs.setFileId(inventory.getId());
        String data = String.join(" ", inventory.getBrand(), inventory.getSize(), inventory.getCategory(),
                inventory.getCreationDate().toString(), inventory.getPrice().toString(),
                Long.toString(inventory.getInStock()), Double.toString(inventory.getWeight()));
        inventoryEs.setData(data);
        repository.save(inventoryEs);
        log.info("Inventory saved to ES {}", inventoryEs);
        return inventoryEs;
    }

    public List<InventoryEs> findByData(String data) throws IOException {
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("data", data).fuzziness("AUTO"));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<InventoryEs> inventoryEsList = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            InventoryEs inventoryEs = new InventoryEs((String) sourceAsMap.get("id"),
                    UUID.fromString((String) sourceAsMap.get("fileId")), (String) sourceAsMap.get("data"));
            inventoryEsList.add(inventoryEs);
        }
        return inventoryEsList;
    }
}

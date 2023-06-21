package com.shoponline.elastic.service;

import com.shoponline.elastic.view.Inventory;
import com.shoponline.elastic.entity.InventoryEs;
import com.shoponline.elastic.repository.InventoryEsRepository;
import com.shoponline.elastic.view.InventoryEsDocument;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
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
        inventoryEs.setInventoryId(inventory.getId());
        String data = String.join(" ", inventory.getBrand(), inventory.getSize(), inventory.getCategory(),
                inventory.getCreationDate().toString(), inventory.getPrice().toString(),
                Long.toString(inventory.getInStock()), Double.toString(inventory.getWeight()));
        inventoryEs.setData(data);
        repository.save(inventoryEs);
        log.info("Inventory saved to ES {}", inventoryEs);
        return inventoryEs;
    }

    public List<InventoryEsDocument> findByData(String data, int from, int size) throws IOException {
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//         basic search
//        searchSourceBuilder.query(QueryBuilders.matchQuery("data", data).fuzziness("AUTO"));
        searchRequest.source(searchSourceBuilder);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .should(new MultiMatchQueryBuilder(data)
                        .field("data", 2.0f)
                        .field("data.prefix", 1.0f)
                        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
                        .operator(Operator.AND)
                        .fuzziness("0")
                ).should(new MultiMatchQueryBuilder(data)
                        .field("data.prefix", 0.5f)
                        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
                        .operator(Operator.AND)
                        .fuzziness("1")
                ).minimumShouldMatch(1);
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort("_score", SortOrder.DESC);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<InventoryEsDocument> inventoryEsDocuments = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            InventoryEsDocument inventoryEsDocument = new InventoryEsDocument();
            inventoryEsDocument.setInventoryId(UUID.fromString((String) sourceAsMap.get("inventoryId")));
            inventoryEsDocument.setData((String) sourceAsMap.get("data"));
            inventoryEsDocument.set_score(hit.getScore());
            inventoryEsDocuments.add(inventoryEsDocument);
        }
        return inventoryEsDocuments;
    }
}

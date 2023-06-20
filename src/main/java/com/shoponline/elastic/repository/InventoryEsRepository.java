package com.shoponline.elastic.repository;

import com.shoponline.elastic.entity.InventoryEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryEsRepository extends ElasticsearchRepository<InventoryEs, String> {

    List<InventoryEs> findByData(String data);
}

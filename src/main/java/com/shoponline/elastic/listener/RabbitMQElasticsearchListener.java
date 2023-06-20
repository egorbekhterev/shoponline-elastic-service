package com.shoponline.elastic.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoponline.elastic.entity.Inventory;
import com.shoponline.elastic.service.InventoryEsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RabbitMQElasticsearchListener {

    private ObjectMapper objectMapper;

    private InventoryEsService service;

    @RabbitListener(queues = "inventoryQueue")
    public void processInventoryUpdate(String message) {
        log.info("Message received from queue: " + message);
        try {
            Inventory inventory = objectMapper.readValue(message, Inventory.class);
            service.create(inventory);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
package com.shoponline.elastic.controller;

import com.shoponline.elastic.entity.InventoryEs;
import com.shoponline.elastic.service.InventoryEsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryEsContoller {

    private InventoryEsService inventoryEsService;

    @GetMapping("/search")
    public List<InventoryEs> search(@RequestParam String data) {
        try {
            return inventoryEsService.findByData(data);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}

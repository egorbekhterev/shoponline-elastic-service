package com.shoponline.elastic.controller;

import com.shoponline.elastic.service.InventoryEsService;
import com.shoponline.elastic.view.InventoryEsDocument;
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
public class InventoryEsController {

    private InventoryEsService inventoryEsService;

    @GetMapping("/search")
    public List<InventoryEsDocument> search(@RequestParam String data, @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        try {
            return inventoryEsService.findByData(data, from, size);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}

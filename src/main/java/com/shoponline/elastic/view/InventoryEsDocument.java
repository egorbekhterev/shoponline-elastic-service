package com.shoponline.elastic.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryEsDocument {

    @JsonProperty("inventory_id")
    private UUID inventoryId;

    @JsonProperty("data")
    private String data;

    @JsonProperty("_score")
    private Float _score;
}

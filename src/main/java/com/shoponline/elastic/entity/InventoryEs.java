package com.shoponline.elastic.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "inventory")
@Setting(settingPath = "/settings.json")
public class InventoryEs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @JsonProperty("inventory_id")
    private UUID inventoryId;

    @Field(type = FieldType.Text, analyzer = "i_prefix", searchAnalyzer = "q_prefix")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String data;
}

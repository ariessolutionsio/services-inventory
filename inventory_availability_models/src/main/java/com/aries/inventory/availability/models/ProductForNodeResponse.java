package com.aries.inventory.availability.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductForNodeResponse {
    private String nodeId;
    private String productKey;
    private List<SkuLineResponse> availableItems;
}

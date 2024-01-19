package com.aries.inventory.availability.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuNodeResponse extends SkuLineResponse {
    private String nodeId;
    public SkuNodeResponse(String nodeId, String sku, BigDecimal availableQuantity) {
        super(sku,availableQuantity);
        this.setNodeId(nodeId);
    }
}

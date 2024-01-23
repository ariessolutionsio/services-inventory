package com.aries.inventory.availability.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    private String skuId;
    private String nodeId;
    private String productKey;
    private BigDecimal quantityOnStock;
}

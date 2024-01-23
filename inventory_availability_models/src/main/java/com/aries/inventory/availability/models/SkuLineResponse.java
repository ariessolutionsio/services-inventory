package com.aries.inventory.availability.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuLineResponse {
    private String skuId;
    private BigDecimal quantity;
}
